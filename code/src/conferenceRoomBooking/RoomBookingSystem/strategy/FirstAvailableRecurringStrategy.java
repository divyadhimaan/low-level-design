package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.Recurrence;
import java.util.List;

public class FirstAvailableRecurringStrategy implements RecurringRoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, Recurrence recurrence, List<Integer> requiredSlots) {
        // Returns the first room that can accommodate recurring booking
        for (Room room : availableRooms) {
            if (room.canBookRecurring(recurrence, requiredSlots)) {
                return room;
            }
        }
        return null;
    }
}
