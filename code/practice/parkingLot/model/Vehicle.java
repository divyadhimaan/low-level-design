package parkingLot.model;

import lombok.Getter;

@Getter
public class Vehicle {
    public String vehicleId;
    public VehicleType vehicleType;

    public Vehicle(String vehicleId, VehicleType vehicleType) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
    }
}
