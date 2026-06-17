package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;
import RoomBookingSystem.model.Recurrence;
import java.util.List;

public class LargestAvailableRecurringStrategy implements RecurringRoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, Recurrence recurrence, List<Integer> requiredSlots) {
        // Prefer larger rooms first for recurring bookings
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.LARGE && room.canBookRecurring(recurrence, requiredSlots)) {
                return room;
            }
        }

        // Fallback to smaller rooms if no large room available
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.SMALL && room.canBookRecurring(recurrence, requiredSlots)) {
                return room;
            }
        }

        return null;
    }
}
