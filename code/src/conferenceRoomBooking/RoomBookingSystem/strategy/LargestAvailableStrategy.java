package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;
import java.util.List;

public class LargestAvailableStrategy implements RoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, List<Integer> requiredSlots) {
        // Prefer larger rooms first, then fallback to smaller rooms
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.LARGE && room.canBook(requiredSlots)) {
                return room;
            }
        }

        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.SMALL && room.canBook(requiredSlots)) {
                return room;
            }
        }

        return null;
    }
}
