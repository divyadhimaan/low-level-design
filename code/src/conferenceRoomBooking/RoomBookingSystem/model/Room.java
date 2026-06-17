package RoomBookingSystem.model;

import lombok.Getter;

import java.util.*;

@Getter
public class Room {
    private final String roomId;
    private final String roomName;
    private final RoomType roomType;
    private Set<Integer> availableSlots;
    private final Map<Integer, Map<Integer, Booking>> bookedSlotsByDay; // dayNumber -> (slotId -> Booking)

    public Room(String roomName, String roomType, Set<Integer> slots)
    {
        this.roomId = roomName + "_" + roomType;
        this.roomName = roomName;
        this.availableSlots = slots;
        this.roomType = RoomType.valueOf(roomType);
        this.bookedSlotsByDay = new HashMap<>();
    }

    public synchronized boolean bookSlots(int day, List<Integer> slots, Booking booking) {
        if (!canBookForDay(day, slots)) return false;

        bookedSlotsByDay.putIfAbsent(day, new HashMap<>());
        Map<Integer, Booking> dayBookings = bookedSlotsByDay.get(day);
        for (int slotId : slots) {
            dayBookings.put(slotId, booking);
        }
        return true;
    }


    public synchronized void displayBookings() {
        if (bookedSlotsByDay.isEmpty()) {
            System.out.println("No bookings for this room yet.");
            return;
        }

        List<Integer> sortedDays = new ArrayList<>(bookedSlotsByDay.keySet());
        Collections.sort(sortedDays);

        System.out.println("==================================================");
        System.out.println("Room: " + roomName + " | Type: " + roomType);
        System.out.println("==================================================");

        for (int day : sortedDays) {
            Map<Integer, Booking> dayBookings = bookedSlotsByDay.get(day);
            System.out.println("Day " + day + ":");

            System.out.printf("%-8s", "Slot");
            for (int slot = 1; slot <= 10; slot++) {
                System.out.printf("%-15d", slot);
            }
            System.out.println();

            System.out.printf("%-8s", "Status");
            for (int slot = 1; slot <= 10; slot++) {
                if (dayBookings.containsKey(slot)) {
                    Booking booking = dayBookings.get(slot);
                    System.out.printf("%-15s", booking.getEmployeeName());
                } else {
                    System.out.printf("%-15s", "Available");
                }
            }
            System.out.println("\n----------------------------------------------------------------------------------------------");
        }
    }



    public synchronized boolean canBookForDay(int day, List<Integer> slots) {
        Map<Integer, Booking> dayBookings = bookedSlotsByDay.getOrDefault(day, new HashMap<>());
        for (int slotId : slots) {
            if (dayBookings.containsKey(slotId)) return false; // conflict
        }
        return true;
    }

    public synchronized boolean canBook(List<Integer> slots) {
        return canBookForDay(0, slots);
    }

    public synchronized boolean canBookRecurring(Recurrence recurrence, List<Integer> requiredSlots) {
        int currentDay = 0;
        int incrementDays = switch (recurrence.getFrequencyType()) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case BIWEEKLY -> 14;
            case MONTHLY -> 30;
            default -> throw new IllegalArgumentException("Unsupported frequency type");
        };

        for (int i = 0; i < recurrence.getNumberOfWeeks(); i++) {
            if (!canBookForDay(currentDay, requiredSlots)) {
                return false; // conflict in at least one occurrence
            }
            currentDay += incrementDays;
        }
        return true;
    }

    public synchronized void cancelBooking(int day, List<Integer> slots) {
        Map<Integer, Booking> dayBookings = bookedSlotsByDay.get(day);
        if (dayBookings != null) {
            for (int slotId : slots) {
                dayBookings.remove(slotId);
            }
        }
    }

}

