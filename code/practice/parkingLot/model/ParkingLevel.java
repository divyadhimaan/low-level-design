package parkingLot.model;

import lombok.Getter;
import java.util.List;

@Getter
public class ParkingLevel {
    public String levelId;
    public List<ParkingSpot> parkingSpots;
    public boolean isFull;


    public ParkingLevel(String levelId, List<ParkingSpot> parkingSpots) {
        this.levelId = levelId;
        this.parkingSpots = parkingSpots;
        this.isFull = false;
    }
}
