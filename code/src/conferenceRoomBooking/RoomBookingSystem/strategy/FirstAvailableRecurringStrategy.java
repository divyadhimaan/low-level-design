package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.Recurrence;

import java.time.LocalTime;
import java.util.List;

public class FirstAvailableRecurringStrategy implements RecurringRoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, Recurrence recurrence) {
        // Returns the first room that can accommodate recurring booking
        for (Room room : availableRooms) {
            if (room.canBookRecurring(recurrence)) {
                return room;
            }
        }
        return null;
    }
}
