package parkingLot.service;

import parkingLot.ParkingLot;
import parkingLot.model.*;
import parkingLot.strategy.SpotAllocationStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ParkingService {
    public Ticket entry(ParkingLot parkingLot, Vehicle vehicle, LocalDateTime time) {
        SpotAllocationStrategy strategy = parkingLot.getSpotAllocationStrategy();
        List<ParkingLevel> levels = parkingLot.getLevels();

        while (true) {
            ParkingSpot spot = strategy.findSpot(vehicle.vehicleType, levels);
            if (spot == null) {
                throw new RuntimeException("No available spot for: " + vehicle.vehicleType);
            }

            synchronized (spot) {
                if (!spot.isOccupied()) {           // re-check under lock
                    spot.occupySpot(vehicle);
                    return parkingLot.getTicketInventory().createTicket(vehicle, spot, time);
                }
                // spot was grabbed by another thread between find and lock → retry
            }
        }
    }

    public void exit(ParkingLot parkingLot, String ticketId, LocalDateTime time) {
        Ticket ticket = parkingLot.getTicketInventory().findTicketById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Invalid ticket ID: " + ticketId);
        }

        synchronized (ticket) {
            if (ticket.getStatus() == TicketStatus.CLOSED) {
                throw new IllegalStateException("Ticket already closed: " + ticketId);
            }

            Duration duration = Duration.between(ticket.getEntryTime(), time);
            long hours = (long) Math.ceil(duration.toMinutes() / 60.0);

            Vehicle vehicle = ticket.getParkingSpot().getParkedVehicle();
            Double amount = parkingLot.getPaymentStrategy().calculateFare(vehicle.getVehicleType(), hours);

            Boolean paymentSuccess = parkingLot.getPaymentService().processPayment(amount);
            if (paymentSuccess) {
                ticket.getParkingSpot().vacateSpot(vehicle);
                ticket.closeTicket(time);
            } else {
                throw new RuntimeException("Payment failed for ticket ID: " + ticketId);
            }
        }
    }
}
