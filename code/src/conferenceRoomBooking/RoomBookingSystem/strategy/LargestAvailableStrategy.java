package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;

import java.time.LocalTime;
import java.util.List;

public class LargestAvailableStrategy implements RoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, LocalTime start, LocalTime end) {
        // Prefer larger rooms first, then fallback to smaller rooms
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.LARGE && room.canBook(start, end)) {
                return room;
            }
        }

        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.SMALL && room.canBook(start, end)) {
                return room;
            }
        }

        return null;
    }
}
