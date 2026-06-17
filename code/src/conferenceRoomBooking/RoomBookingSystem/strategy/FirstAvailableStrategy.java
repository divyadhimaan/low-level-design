package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import java.util.List;

public class FirstAvailableStrategy implements RoomStrategy {

    @Override
    public Room selectRoom(List<Room> availableRooms, List<Integer> requiredSlots) {
        // Returns the first available room without any preference
        for (Room room : availableRooms) {
            if (room.canBook(requiredSlots)) {
                return room;
            }
        }
        return null;
    }
}
