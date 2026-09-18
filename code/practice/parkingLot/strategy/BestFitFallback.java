package parkingLot.strategy;

import parkingLot.model.ParkingLevel;
import parkingLot.model.ParkingSpot;
import parkingLot.model.SpotType;
import parkingLot.model.VehicleType;

import java.util.List;
import java.util.Map;

public class BestFitFallback implements SpotAllocationStrategy{

    private static final Map<VehicleType, List<SpotType>> FALLBACK_ORDER = Map.of(
            VehicleType.BIKE,  List.of(SpotType.SMALL, SpotType.MEDIUM, SpotType.LARGE),
            VehicleType.CAR,   List.of(SpotType.MEDIUM, SpotType.LARGE),
            VehicleType.TRUCK, List.of(SpotType.LARGE)
    );

    @Override
    public ParkingSpot findSpot(VehicleType type, List<ParkingLevel> levels) {
        for (SpotType candidate : FALLBACK_ORDER.get(type)) {
            for (ParkingLevel level : levels) {
                for (ParkingSpot spot : level.getParkingSpots()) {
                    if (spot.getSpotType() == candidate && !spot.isOccupied()) {
                        return spot;
                    }
                }
            }
        }
        return null;
    }
}
