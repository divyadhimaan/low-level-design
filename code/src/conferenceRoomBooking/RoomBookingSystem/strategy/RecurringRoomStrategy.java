package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.Recurrence;
import java.util.List;

public interface RecurringRoomStrategy {
    Room selectRoom(List<Room> availableRooms, Recurrence recurrence, List<Integer> requiredSlots);
}
