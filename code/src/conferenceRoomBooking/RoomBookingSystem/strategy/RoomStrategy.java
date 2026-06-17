package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import java.util.List;

public interface RoomStrategy {
    Room selectRoom(List<Room> availableRooms, List<Integer> requiredSlots);
}
