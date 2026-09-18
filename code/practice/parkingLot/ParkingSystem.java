package parkingLot;

import parkingLot.model.Ticket;
import parkingLot.model.Vehicle;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingSystem {
    private static ParkingSystem instance;
    private final Map<String, ParkingLot> parkingLotMap;

    private ParkingSystem() {
        this.parkingLotMap = new ConcurrentHashMap<>();
    }

    public static synchronized ParkingSystem getInstance(){
        if(instance == null){
            instance = new ParkingSystem();
        }
        return instance;
    }

    public void registerLot(ParkingLot parkingLot) {
        parkingLotMap.put(parkingLot.getParkingLotId(), parkingLot);
    }

    public Ticket invokeEntry(String parkingLotId, Vehicle vehicle) {
        ParkingLot parkingLot = parkingLotMap.get(parkingLotId);
        if (parkingLot == null) {
            throw new IllegalArgumentException("Invalid parking lot ID");
        }
        return parkingLot.getParkingService().entry(parkingLot, vehicle, LocalDateTime.now());
    }

    public void invokeExit(String parkingLotId, String ticketId) {
        ParkingLot parkingLot = parkingLotMap.get(parkingLotId);
        if (parkingLot == null) {
            throw new IllegalArgumentException("Invalid parking lot ID");
        }
        try{
            parkingLot.getParkingService().exit(parkingLot, ticketId, LocalDateTime.now());
        } catch (RuntimeException e) {
            System.out.println("Error during exit: " + e.getMessage());
        }

    }
}
