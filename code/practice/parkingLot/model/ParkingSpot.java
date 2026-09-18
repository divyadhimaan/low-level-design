package parkingLot.model;

import lombok.Getter;

@Getter
public class ParkingSpot {
    public String spotId;
    public SpotType spotType;
    public volatile boolean isOccupied;
    public Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.isOccupied = false;
    }

    public synchronized void occupySpot(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.isOccupied = true;
    }

    public synchronized void vacateSpot(Vehicle vehicle) {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }

}
