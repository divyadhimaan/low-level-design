package parkingLot.strategy;

import parkingLot.model.ParkingLevel;
import parkingLot.model.ParkingSpot;
import parkingLot.model.VehicleType;

import java.util.List;

public interface SpotAllocationStrategy {
    public ParkingSpot findSpot(VehicleType type, List<ParkingLevel> levels);
}
