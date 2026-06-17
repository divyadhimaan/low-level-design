package RoomBookingSystem.repository;

import RoomBookingSystem.model.Room;
import RoomBookingSystem.model.RoomType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomInventory {

    Map<RoomType,Map<String, Room>> roomList;

    public RoomInventory() {
        this.roomList = new ConcurrentHashMap<>();
        for (RoomType type : RoomType.values()) {
            roomList.put(type, new ConcurrentHashMap<>());
        }
    }

    public synchronized void addRoom(Room room) {
        roomList.get(room.getRoomType()).put(room.getRoomId(), room);
    }

    public synchronized Room getRoom(String roomId) {
       return getAllRooms().get(roomId);
    }
    public synchronized Map<String, Room> getAllRooms() {
        Map<String, Room> allRooms = new HashMap<>();
        for (Map<String, Room> rooms : roomList.values()) {
            allRooms.putAll(rooms);
        }
        return allRooms;
    }
    public synchronized Map<String, Room> getAllRoomsByType(RoomType roomType) {
        return new HashMap<>(roomList.getOrDefault(roomType, new HashMap<>()));
    }
}
