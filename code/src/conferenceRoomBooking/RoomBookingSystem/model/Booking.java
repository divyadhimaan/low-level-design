package RoomBookingSystem.model;

import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
public class Booking {
    private final UUID bookingId;
    private final Room room;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String employeeName;
    private final int day;

    public Booking(Room room, LocalTime startTime, LocalTime endTime, String employeeName, int day) {
        this.bookingId = UUID.randomUUID();
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeName = employeeName;
        this.day = day;
    }
}
