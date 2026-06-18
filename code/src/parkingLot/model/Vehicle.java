package model;

public abstract class Vehicle {
    protected String licensePlate;
    protected VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType type)
    {
        this.licensePlate = licensePlate;
        this.vehicleType = type;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public String getLicenseNumber()
    {
        return licensePlate;
    }
}
