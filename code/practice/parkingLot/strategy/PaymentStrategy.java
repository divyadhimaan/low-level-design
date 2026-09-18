package parkingLot.strategy;

import parkingLot.model.VehicleType;

import java.time.LocalDateTime;

public interface PaymentStrategy {
    public Double calculateFare(VehicleType type, Long hoursParked);
}
