package model;

import java.util.List;

public class ParkingLevel {
    private int levelId;
    private List<ParkingSpot> parkingSpots;

    public ParkingLevel(int levelId, List<ParkingSpot> spots) {
        this.levelId = levelId;
        this.parkingSpots = spots;
    }

    public ParkingSpot findParkingSpot(VehicleType type)
    {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied() && spot.getSpotType().canFitVehicle(type)) {
                return spot;
            }
        }
        return null;
    }

    public boolean isFull() {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied()) {
                return false;
            }
        }
        return true;
    }

}
