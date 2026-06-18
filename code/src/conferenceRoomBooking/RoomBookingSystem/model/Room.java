package RoomBookingSystem.model;

import lombok.Getter;

import java.time.LocalTime;
import java.util.*;

@Getter
public class Room {
    private final String roomId;
    private final String roomName;
    private final RoomType roomType;
    // day index -> sorted map of (bookingStartTime -> Booking)
    // TreeMap gives O(log n) conflict detection via floorEntry / ceilingEntry
    private final Map<Integer, TreeMap<LocalTime, Booking>> bookingsByDay;

    public Room(String roomName, String roomType) {
        this.roomId = roomName + "_" + roomType;
        this.roomName = roomName;
        this.roomType = RoomType.valueOf(roomType);
        this.bookingsByDay = new HashMap<>();
    }

    /**
     * Book a time window on the given day. Caller must hold the lock on this Room.
     */
    public synchronized boolean bookSlots(int day, LocalTime start, LocalTime end, Booking booking) {
        if (!canBookForDay(day, start, end)) return false;
        bookingsByDay.computeIfAbsent(day, d -> new TreeMap<>()).put(start, booking);
        return true;
    }

    /**
     * O(log n) conflict check using TreeMap.
     *
     * Only two existing bookings can possibly overlap [start, end):
     *   1. floorEntry(start)  – the booking whose start ≤ our start; still running if its end > our start.
     *   2. ceilingEntry(start) – the next booking after ours; conflicts if its start < our end.
     */
    public synchronized boolean canBookForDay(int day, LocalTime start, LocalTime end) {
        TreeMap<LocalTime, Booking> dayMap = bookingsByDay.get(day);
        if (dayMap == null || dayMap.isEmpty()) return true;

        // Predecessor: a booking that started at or before our start time
        Map.Entry<LocalTime, Booking> before = dayMap.floorEntry(start);
        if (before != null && before.getValue().getEndTime().isAfter(start)) {
            return false; // predecessor is still running when we want to start
        }

        // Successor: the next booking that starts after our start time
        Map.Entry<LocalTime, Booking> after = dayMap.ceilingEntry(start);
        if (after != null && after.getKey().isBefore(end)) {
            return false; // successor starts before we finish
        }

        return true;
    }

    /** Convenience – checks day 0 (used for single-day bookings). */
    public synchronized boolean canBook(LocalTime start, LocalTime end) {
        return canBookForDay(0, start, end);
    }

    public synchronized boolean canBookRecurring(Recurrence recurrence) {
        int currentDay = recurrence.getDayOfWeek();
        int incrementDays = switch (recurrence.getFrequencyType()) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case BIWEEKLY -> 14;
            case MONTHLY -> 30;
            default -> throw new IllegalArgumentException("Unsupported frequency type");
        };
        int totalOccurrences = recurrence.getFrequencyType() == FrequencyType.DAILY
                ? recurrence.getNumberOfWeeks() * 7
                : recurrence.getNumberOfWeeks();

        for (int i = 0; i < totalOccurrences; i++) {
            if (!canBookForDay(currentDay, recurrence.getStart(), recurrence.getEnd())) return false;
            currentDay += incrementDays;
        }
        return true;
    }

    /** Remove a booking by its start time (used for rollback). */
    public synchronized void cancelBooking(int day, LocalTime start) {
        TreeMap<LocalTime, Booking> dayMap = bookingsByDay.get(day);
        if (dayMap != null) {
            dayMap.remove(start);
            if (dayMap.isEmpty()) bookingsByDay.remove(day);
        }
    }

    public synchronized void displayBookings() {
        if (bookingsByDay.isEmpty()) {
            System.out.println("No bookings for this room yet.");
            return;
        }

        List<Integer> sortedDays = new ArrayList<>(bookingsByDay.keySet());
        Collections.sort(sortedDays);

        System.out.println("==================================================");
        System.out.println("Room: " + roomName + " | Type: " + roomType);
        System.out.println("==================================================");

        for (int day : sortedDays) {
            System.out.println("Day " + day + ":");
            System.out.printf("  %-20s %-20s %-20s %s%n", "Start", "End", "Employee", "Booking ID");
            System.out.println("  " + "-".repeat(80));
            for (Map.Entry<LocalTime, Booking> entry : bookingsByDay.get(day).entrySet()) {
                Booking b = entry.getValue();
                System.out.printf("  %-20s %-20s %-20s %s%n",
                        b.getStartTime(), b.getEndTime(), b.getEmployeeName(), b.getBookingId());
            }
            System.out.println();
        }
    }
}

