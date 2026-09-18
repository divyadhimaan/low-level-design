package parkingLot.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Ticket {
    private final String ticketId;
    private final String vehicleId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private final ParkingSpot parkingSpot;
    private TicketStatus status;

    private Ticket(Builder builder) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicleId = builder.vehicleId;
        this.entryTime = builder.entryTime;
        this.parkingSpot = builder.parkingSpot;
        this.status = builder.status;
    }

    public synchronized void closeTicket(LocalDateTime exitTime) {
        this.exitTime = exitTime;
        this.status = TicketStatus.CLOSED;
    }

    public static class Builder {
        private String vehicleId;
        private LocalDateTime entryTime;
        private ParkingSpot parkingSpot;
        private TicketStatus status;

        public Builder vehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder entryTime(LocalDateTime entryTime) {
            this.entryTime = entryTime;
            return this;
        }

        public Builder parkingSpot(ParkingSpot parkingSpot) {
            this.parkingSpot = parkingSpot;
            return this;
        }

        public Builder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}
