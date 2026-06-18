package RoomBookingSystem.observer;

import RoomBookingSystem.model.Booking;
import java.util.List;

public class SlackObserver implements BookingObserver {

    @Override
    public void onBookingCreated(Booking booking) {
        String message = String.format(
            ":calendar: **Room Booking Confirmation**\n" +
            "Employee: %s\n" +
            "Room: %s\n" +
            "Day: %d | Time: %s – %s\n" +
            "Booking ID: %s",
            booking.getEmployeeName(),
            booking.getRoom().getRoomName(),
            booking.getDay(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getBookingId()
        );
        sendSlackMessage("#room-bookings", message);
    }

    @Override
    public void onRecurringBookingCreated(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String message = String.format(
            ":calendar: **Recurring Room Booking Confirmed**\n" +
            "Employee: %s\n" +
            "Room: %s\n" +
            "Occurrences: %d\n" +
            "First Booking - Day: %d, Time: %s – %s",
            firstBooking.getEmployeeName(),
            firstBooking.getRoom().getRoomName(),
            bookings.size(),
            firstBooking.getDay(),
            firstBooking.getStartTime(),
            firstBooking.getEndTime()
        );
        sendSlackMessage("#room-bookings", message);
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        String message = String.format(
            ":x: **Room Booking Cancelled**\n" +
            "Employee: %s\n" +
            "Room: %s\n" +
            "Booking ID: %s",
            booking.getEmployeeName(),
            booking.getRoom().getRoomName(),
            booking.getBookingId()
        );
        sendSlackMessage("#room-bookings", message);
    }

    @Override
    public void onRecurringBookingCancelled(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String message = String.format(
            ":x: **Recurring Room Booking Cancelled**\n" +
            "Employee: %s\n" +
            "Room: %s\n" +
            "Occurrences Cancelled: %d",
            firstBooking.getEmployeeName(),
            firstBooking.getRoom().getRoomName(),
            bookings.size()
        );
        sendSlackMessage("#room-bookings", message);
    }

    private void sendSlackMessage(String channel, String message) {
        System.out.println("======== SLACK NOTIFICATION ========");
        System.out.println("Channel: " + channel);
        System.out.println("Message:\n" + message);
        System.out.println("====================================");
        // In production: send via Slack API (webhooks or bot API)
    }
}
