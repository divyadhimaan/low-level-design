package parkingLot;

import parkingLot.model.*;
import parkingLot.strategy.BestFitFallback;
import parkingLot.strategy.HourlyPayment;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        // --- Setup ---
        ParkingLevel level1 = new ParkingLevel("L1", List.of(
                new ParkingSpot("S1", SpotType.SMALL),
                new ParkingSpot("S2", SpotType.SMALL),
                new ParkingSpot("S3", SpotType.MEDIUM),
                new ParkingSpot("S4", SpotType.MEDIUM),
                new ParkingSpot("S5", SpotType.LARGE)
        ));

        ParkingLot lot = new ParkingLot(
                "LOT1",
                List.of(level1),
                new BestFitFallback(),
                new HourlyPayment()
        );

        ParkingSystem system = ParkingSystem.getInstance();
        system.registerLot(lot);

        // --- Entry ---
        Vehicle bike = new Vehicle("BIKE-001", VehicleType.BIKE);
        Vehicle car = new Vehicle("CAR-001", VehicleType.CAR);
        Vehicle truck = new Vehicle("TRUCK-001", VehicleType.TRUCK);

        Ticket bikeTicket = system.invokeEntry("LOT1", bike);
        System.out.println("Bike entered  → ticket: " + bikeTicket.getTicketId()
                + ", spot: " + bikeTicket.getParkingSpot().getSpotId());

        Ticket carTicket = system.invokeEntry("LOT1", car);
        System.out.println("Car entered   → ticket: " + carTicket.getTicketId()
                + ", spot: " + carTicket.getParkingSpot().getSpotId());

        Ticket truckTicket = system.invokeEntry("LOT1", truck);
        System.out.println("Truck entered → ticket: " + truckTicket.getTicketId()
                + ", spot: " + truckTicket.getParkingSpot().getSpotId());

        // --- Exit ---
        system.invokeExit("LOT1", bikeTicket.getTicketId());
        System.out.println("Bike exited   → spot " + bikeTicket.getParkingSpot().getSpotId()
                + " occupied: " + bikeTicket.getParkingSpot().getIsOccupied());

        system.invokeExit("LOT1", carTicket.getTicketId());
        System.out.println("Car exited    → spot " + carTicket.getParkingSpot().getSpotId()
                + " occupied: " + carTicket.getParkingSpot().getIsOccupied());

        // --- Re-entry after spot freed ---
        Vehicle car2 = new Vehicle("CAR-002", VehicleType.CAR);
        Ticket car2Ticket = system.invokeEntry("LOT1", car2);
        System.out.println("Car2 entered  → ticket: " + car2Ticket.getTicketId()
                + ", spot: " + car2Ticket.getParkingSpot().getSpotId());

        // --- Full lot scenario ---
        Vehicle extraBike = new Vehicle("BIKE-002", VehicleType.BIKE);
        Ticket bike2Ticket = system.invokeEntry("LOT1", extraBike);
        System.out.println("Bike2 entered → ticket: " + bike2Ticket.getTicketId()
                + ", spot: " + bike2Ticket.getParkingSpot().getSpotId());

        Vehicle bike3 = new Vehicle("BIKE-003", VehicleType.BIKE);
        System.out.println("Trying BIKE-003 (no SMALL spots left)...");
        system.invokeEntry("LOT1", bike3); // should throw — no more SMALL spots
    }
}
