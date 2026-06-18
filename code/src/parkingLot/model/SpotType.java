package model;

public enum SpotType {
    CAR(VehicleType.CAR),
    BIKE(VehicleType.BIKE),
    TRUCK(VehicleType.TRUCK);

    private final VehicleType compatibleVehicleType;

    SpotType(VehicleType compatibleVehicleType) {
        this.compatibleVehicleType = compatibleVehicleType;
    }

    public boolean canFitVehicle(VehicleType vehicleType) {
        return this.compatibleVehicleType == vehicleType;
    }
}