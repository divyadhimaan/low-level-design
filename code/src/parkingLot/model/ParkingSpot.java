package model;

public class ParkingSpot {
    private int spotId;
    private SpotType spotType;
    private boolean isOccupied;
    private Vehicle parkedVehicle;

    public ParkingSpot(SpotType spotType, int spotId)
    {
        this.isOccupied = false;
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public void occupySpot(Vehicle vehicle)
    {
        this.parkedVehicle = vehicle;
        this.isOccupied = true;
    }
    public void vacateSpot()
    {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public SpotType getSpotType(){
        return spotType;
    }
}

