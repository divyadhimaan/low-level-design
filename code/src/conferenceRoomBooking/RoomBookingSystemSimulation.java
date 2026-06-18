
import RoomBookingSystem.service.RoomBookingSystem;
import RoomBookingSystem.model.Recurrence;
import RoomBookingSystem.model.Recurrence.Builder;
import RoomBookingSystem.observer.BookingObserver;
import RoomBookingSystem.observer.EmailObserver;
import RoomBookingSystem.observer.CalendarObserver;
import RoomBookingSystem.observer.SlackObserver;

import java.time.LocalTime;
import java.util.List;

public class RoomBookingSystemSimulation {
    public static void main(String[] args) {

        RoomBookingSystem bookingRoomSystem = RoomBookingSystem.getInstance();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    Room Booking System Initialized      ║");
        System.out.println("║    (Observers: Email, Calendar, Slack)  ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        List<Integer> slots1 = List.of(1,2,3,4,5,6,7,8,9,10);
        bookingRoomSystem.registerRoom("Room A", "SMALL", slots1);
        bookingRoomSystem.registerRoom("Room B", "LARGE", slots1);

        //check validation for duplicate room name
        bookingRoomSystem.registerRoom("Room A", "SMALL", slots1);

        bookingRoomSystem.showAllRegisteredRooms();

        bookingRoomSystem.registerEmployee("Alice", "Marketing");
        bookingRoomSystem.registerEmployee("Bob", "Sales");
        bookingRoomSystem.registerEmployee("David", "IT");

        //check validation for duplicate employee name
        bookingRoomSystem.registerEmployee("Bob", "Sales");

        bookingRoomSystem.showAllRegisteredEmployees();

        // ====== SINGLE BOOKINGS ======
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        Creating Single Bookings         ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        bookingRoomSystem.bookRoom("Alice",   7,  LocalTime.of(10, 0), LocalTime.of(10, 30));
        bookingRoomSystem.bookRoom("Bob",     12, LocalTime.of(9,  0), LocalTime.of(10, 30));
        bookingRoomSystem.bookRoom("Charlie", 3,  LocalTime.of(9,  0), LocalTime.of(10, 30)); // fails: Charlie not registered
        bookingRoomSystem.bookRoom("Alice",   7,  LocalTime.of(10, 0), LocalTime.of(13, 0));

        // ====== RECURRING BOOKINGS WITH BUILDER PATTERN ======
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      Creating Recurring Bookings        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Using Builder Pattern for Recurrence - Much more readable!
        var weeklyRecurrence = new Builder(3, LocalTime.of(11,0,0), LocalTime.of(12,0,0), 3)
                .withFrequency("WEEKLY")
                .build();
        System.out.println("📋 Weekly recurrence built: " + weeklyRecurrence);
        bookingRoomSystem.bookRoomRecurring("David", 5, weeklyRecurrence);

        var dailyRecurrence = new Builder(2, LocalTime.of(13,0,0), LocalTime.of(14,0,0), 1)
                .withFrequency("DAILY")
                .build();
        System.out.println("📋 Daily recurrence built: " + dailyRecurrence);
        bookingRoomSystem.bookRoomRecurring("Bob", 13, dailyRecurrence);

        bookingRoomSystem.viewSchedule();

    }
}
