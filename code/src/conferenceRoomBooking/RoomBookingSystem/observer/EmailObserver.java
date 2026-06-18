package RoomBookingSystem.observer;

import RoomBookingSystem.model.Booking;
import java.util.List;

public class EmailObserver implements BookingObserver {

    @Override
    public void onBookingCreated(Booking booking) {
        String emailBody = String.format(
            "Your room booking has been confirmed!%n" +
            "Room: %s%n" +
            "Time Slot: %s%n" +
            "Duration: %s minutes%n" +
            "Day: %d%n" +
            "Booking ID: %s",
            booking.getRoom().getRoomName(),
            booking.getBookedSlots(),
            "60",  // Default or calculate from slots
            booking.getDay(),
            booking.getBookingId()
        );
        sendEmail(booking.getEmployeeName(), "Room Booking Confirmation", emailBody);
    }

    @Override
    public void onRecurringBookingCreated(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String emailBody = String.format(
            "Your recurring room booking has been confirmed!%n" +
            "Room: %s%n" +
            "Total Occurrences: %d%n" +
            "Booking Details:%n%s%n" +
            "You will receive individual reminders for each occurrence.",
            firstBooking.getRoom().getRoomName(),
            bookings.size(),
            formatBookingDetails(bookings)
        );
        sendEmail(firstBooking.getEmployeeName(), "Recurring Room Booking Confirmation", emailBody);
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        String emailBody = String.format(
            "Your room booking has been cancelled.%n" +
            "Room: %s%n" +
            "Booking ID: %s%n" +
            "If you did not request this cancellation, please contact support.",
            booking.getRoom().getRoomName(),
            booking.getBookingId()
        );
        sendEmail(booking.getEmployeeName(), "Room Booking Cancelled", emailBody);
    }

    @Override
    public void onRecurringBookingCancelled(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        Booking firstBooking = bookings.get(0);
        String emailBody = String.format(
            "Your recurring room booking has been cancelled.%n" +
            "Room: %s%n" +
            "Occurrences Cancelled: %d%n" +
            "If you did not request this cancellation, please contact support.",
            firstBooking.getRoom().getRoomName(),
            bookings.size()
        );
        sendEmail(firstBooking.getEmployeeName(), "Recurring Room Booking Cancelled", emailBody);
    }

    private String formatBookingDetails(List<Booking> bookings) {
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            details.append(String.format("  %d. Day: %d, Slots: %s%n",
                i + 1, booking.getDay(), booking.getBookedSlots()));
        }
        return details.toString();
    }

    private void sendEmail(String employeeName, String subject, String body) {
        System.out.println("======== EMAIL NOTIFICATION ========");
        System.out.println("To: " + employeeName + "@company.com");
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
        System.out.println("====================================");
        // In production: send actual email via SMTP, SendGrid, etc.
    }
}
