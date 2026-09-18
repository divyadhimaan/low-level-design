package parkingLot.inventory;

import parkingLot.model.ParkingSpot;
import parkingLot.model.Ticket;
import parkingLot.model.TicketStatus;
import parkingLot.model.Vehicle;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TicketInventory {
    public Map<String, Ticket> ticketMap;

    public TicketInventory() {
        this.ticketMap = new ConcurrentHashMap<>();
    }

    private void addTicket(Ticket ticket) {
        ticketMap.put(ticket.getTicketId(), ticket);
    }

    public Ticket createTicket(Vehicle vehicle, ParkingSpot parkingSpot, LocalDateTime entryTime) {
        Ticket ticket = new Ticket.Builder()
                .vehicleId(vehicle.getVehicleId())
                .parkingSpot(parkingSpot)
                .entryTime(entryTime)
                .status(TicketStatus.OPEN)
                .build();
        addTicket(ticket);
        return ticket;
    }

    public Ticket findTicketById(String ticketId) {
        return ticketMap.get(ticketId);
    }

}
