package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;

import java.time.LocalTime;
import java.util.List;

public class FirstAvailableStrategy implements RoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, LocalTime start, LocalTime end) {
        // Returns the first available room without any preference
        for (Room room : availableRooms) {
            if (room.canBook(start, end)) {
                return room;
            }
        }
        return null;
    }
}
