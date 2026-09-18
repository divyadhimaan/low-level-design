package parkingLot.strategy;

import parkingLot.model.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class HourlyPayment implements PaymentStrategy{
    private final Map<VehicleType, Double> hourlyRates;

    public HourlyPayment() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.BIKE, 2.0);
        hourlyRates.put(VehicleType.CAR, 5.0);
        hourlyRates.put(VehicleType.TRUCK, 10.0);
    }

    @Override
    public Double calculateFare(VehicleType type, Long hoursParked) {
        return hourlyRates.getOrDefault(type, 0.0) * Math.max(hoursParked, 1);
    }
}
