package facade;

import model.*;
import repository.TicketInventory;
import service.PaymentService;
import strategy.PaymentStrategy;


import java.time.LocalTime;
import java.util.*;

public class ParkingLot {
    private static ParkingLot instance;
    private final List<ParkingLevel> parkingLevels;
    private PaymentStrategy strategy;
    private TicketInventory ticketInventory;
    private PaymentService paymentService;

    private ParkingLot(PaymentStrategy strategy, int levels)
    {
        this.strategy = strategy;
        this.ticketInventory = new TicketInventory();
        this.paymentService = new PaymentService();
        this.parkingLevels = initializeLevels(levels);
    }

    private List<ParkingLevel> initializeLevels(int totalLevels) {
        List<ParkingLevel> levels = new ArrayList<>();
        for (int i = 0; i < totalLevels; i++) {
            List<ParkingSpot> spots = new ArrayList<>();
            int spotId = 0;
            spots.add(new ParkingSpot(SpotType.BIKE, spotId++));
            spots.add(new ParkingSpot(SpotType.BIKE, spotId++));
            spots.add(new ParkingSpot(SpotType.CAR, spotId++));
            spots.add(new ParkingSpot(SpotType.CAR, spotId++));
            spots.add(new ParkingSpot(SpotType.CAR, spotId++));
            spots.add(new ParkingSpot(SpotType.TRUCK, spotId++));
            levels.add(new ParkingLevel(i, spots));
        }
        return levels;
    }

    public static synchronized ParkingLot getInstance(PaymentStrategy strategy, int totalLevels) {
        if (instance == null)
            instance = new ParkingLot(strategy, totalLevels);
        return instance;
    }

    public ParkingLotTicket entry(Vehicle vehicle, LocalTime entryTime){
        ParkingSpot spot = null;
        synchronized (this) {
            spot = findAvailableSpot(vehicle.getVehicleType());

            if (spot == null) {
                System.out.println("No available parking spot for vehicle: " + vehicle.getLicenseNumber());
                return null;
            }

            spot.occupySpot(vehicle);
        }

        ParkingLotTicket ticket = new ParkingLotTicket.Builder()
                .entryTime(entryTime)
                .parkingSpot(spot)
                .vehicle(vehicle)
                .build();

        ticketInventory.addTicket(ticket);

        return ticket;
    }

    public void exit(LocalTime exitTime, UUID ticketId){
        ParkingLotTicket ticket = ticketInventory.getTicketById(ticketId);

        if(ticket == null){
            throw new IllegalArgumentException("No Vehicle entered with this ID");
        }


        System.out.println("Valid Ticket Found. Processing Payment");

        Double amount = paymentService.calculatePaymentAmount(ticket, strategy, exitTime);
        System.out.println("Payable Amount: " + amount);
        paymentService.processPayment(amount, ticket);

        synchronized (this) {
            ticket.getParkingSpot().vacateSpot();
        }

        ticket.markCompleted();
    }

    public ParkingSpot findAvailableSpot(VehicleType vehicleType)
    {
        for (ParkingLevel level : parkingLevels) {
            ParkingSpot spot = level.findParkingSpot(vehicleType);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }


}
