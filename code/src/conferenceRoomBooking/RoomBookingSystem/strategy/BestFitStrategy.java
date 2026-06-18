package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;

import java.time.LocalTime;
import java.util.List;

public class BestFitStrategy implements RoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, LocalTime start, LocalTime end) {
        // Prefer smaller rooms first, then fallback to larger rooms
        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.SMALL && room.canBook(start, end)) {
                return room;
            }
        }

        for (Room room : availableRooms) {
            if (room.getRoomType() == RoomType.LARGE && room.canBook(start, end)) {
                return room;
            }
        }

        return null;
    }
}
