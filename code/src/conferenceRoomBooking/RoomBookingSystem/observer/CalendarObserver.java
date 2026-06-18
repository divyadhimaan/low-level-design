package RoomBookingSystem.observer;

import RoomBookingSystem.model.Booking;
import java.util.List;

public class CalendarObserver implements BookingObserver {

    @Override
    public void onBookingCreated(Booking booking) {
        String eventDetails = String.format(
            "Calendar Event Created:%n" +
            "Title: Meeting in %s%n" +
            "Employee: %s%n" +
            "Room: %s%n" +
            "Day: %d%n" +
            "Time: %s – %s%n" +
            "Event ID: %s",
            booking.getRoom().getRoomName(),
            booking.getEmployeeName(),
            booking.getRoom().getRoomName(),
            booking.getDay(),
            booking.getStartTime(),
            booking.getEndTime(),
            "CALENDAR-" + booking.getBookingId()
        );
        addToCalendar(booking.getEmployeeName(), eventDetails);
    }

    @Override
    public void onRecurringBookingCreated(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String eventDetails = String.format(
            "Recurring Calendar Events Created:%n" +
            "Title: %s Meeting Series%n" +
            "Employee: %s%n" +
            "Room: %s%n" +
            "Total Occurrences: %d%n" +
            "First Occurrence - Day: %d, Time: %s – %s",
            firstBooking.getRoom().getRoomName(),
            firstBooking.getEmployeeName(),
            firstBooking.getRoom().getRoomName(),
            bookings.size(),
            firstBooking.getDay(),
            firstBooking.getStartTime(),
            firstBooking.getEndTime()
        );

        for (Booking booking : bookings) {
            addToCalendar(booking.getEmployeeName(), eventDetails);
        }
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        String eventDetails = String.format(
            "Calendar Event Deleted:%n" +
            "Event ID: %s%n" +
            "Room: %s%n" +
            "Reason: Booking Cancelled%n" +
            "Employee: %s",
            "CALENDAR-" + booking.getBookingId(),
            booking.getRoom().getRoomName(),
            booking.getEmployeeName()
        );
        deleteFromCalendar(booking.getEmployeeName(), eventDetails);
    }

    @Override
    public void onRecurringBookingCancelled(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String eventDetails = String.format(
            "Recurring Calendar Events Deleted:%n" +
            "Room: %s%n" +
            "Occurrences Deleted: %d%n" +
            "Reason: Recurring Booking Cancelled%n" +
            "Employee: %s",
            firstBooking.getRoom().getRoomName(),
            bookings.size(),
            firstBooking.getEmployeeName()
        );

        for (Booking booking : bookings) {
            deleteFromCalendar(booking.getEmployeeName(), eventDetails);
        }
    }

    private void addToCalendar(String employeeName, String eventDetails) {
        System.out.println("======== CALENDAR SYNC ========");
        System.out.println("User: " + employeeName);
        System.out.println("Action: ADD");
        System.out.println(eventDetails);
        System.out.println("===============================");
        // In production: sync with Google Calendar, Outlook, etc. via API
    }

    private void deleteFromCalendar(String employeeName, String eventDetails) {
        System.out.println("======== CALENDAR SYNC ========");
        System.out.println("User: " + employeeName);
        System.out.println("Action: DELETE");
        System.out.println(eventDetails);
        System.out.println("===============================");
        // In production: sync with Google Calendar, Outlook, etc. via API
    }
}
