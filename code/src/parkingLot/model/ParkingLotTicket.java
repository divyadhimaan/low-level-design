package model;

import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
public class ParkingLotTicket {
    private final UUID ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalTime entryTime;

    public ParkingLotTicket(Builder builder) {
        this.ticketId = builder.ticketId;
        this.vehicle = builder.vehicle;
        this.parkingSpot = builder.parkingSpot;
        this.entryTime = builder.entryTime;
    }

    public static class Builder {
        private UUID ticketId;
        private Vehicle vehicle;
        private ParkingSpot parkingSpot;
        private LocalTime entryTime;


        public Builder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public Builder parkingSpot(ParkingSpot parkingSpot) {
            this.parkingSpot = parkingSpot;
            return this;
        }

        public Builder entryTime(LocalTime entryTime) {
            this.entryTime = entryTime;
            return this;
        }

        public ParkingLotTicket build() {
            if (vehicle == null || parkingSpot == null || entryTime == null) {
                throw new IllegalStateException("ticketId, vehicle, and parkingSpot are required");
            }
            this.ticketId = UUID.randomUUID();
            return new ParkingLotTicket(this);
        }
    }
}
