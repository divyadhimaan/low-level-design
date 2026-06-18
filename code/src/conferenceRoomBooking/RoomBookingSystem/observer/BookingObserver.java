package RoomBookingSystem.observer;

import RoomBookingSystem.model.Booking;
import java.util.List;

public interface BookingObserver {
    void onBookingCreated(Booking booking);
    void onRecurringBookingCreated(List<Booking> bookings);
    void onBookingCancelled(Booking booking);
    void onRecurringBookingCancelled(List<Booking> bookings);
}
