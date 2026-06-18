package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;
import RoomBookingSystem.model.Recurrence;

import java.time.LocalTime;
import java.util.List;

public class BestFitRecurringStrategy implements RecurringRoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, Recurrence recurrence) {
        // Prefer smaller rooms first for recurring bookings
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.SMALL && room.canBookRecurring(recurrence)) {
                return room;
            }
        }

        // Fallback to larger rooms if no small room available
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.LARGE && room.canBookRecurring(recurrence)) {
                return room;
            }
        }

        return null;
    }
}
