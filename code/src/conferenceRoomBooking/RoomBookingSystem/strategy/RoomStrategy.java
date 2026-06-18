package RoomBookingSystem.strategy;

import RoomBookingSystem.model.Room;

import java.time.LocalTime;
import java.util.List;

public interface RoomStrategy {
    Room selectRoom(List<Room> availableRooms, LocalTime start, LocalTime end);
}
