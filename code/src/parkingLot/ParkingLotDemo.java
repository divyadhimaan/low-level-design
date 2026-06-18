import model.ParkingLotTicket;
import model.Vehicle;
import model.Car;
import facade.ParkingLot;
import strategy.HourlyPaymentStrategy;

import java.time.LocalTime;

public class ParkingLotDemo {
    public static void main(String[] args)
    {
        ParkingLot parkingLot = ParkingLot.getInstance(new HourlyPaymentStrategy(), 4);


        Vehicle car = new Car("KA-01-AB-1234");
        ParkingLotTicket ticket = parkingLot.entry(car, LocalTime.now());

        if (ticket != null) {
            try {
                Thread.sleep(5000); // Simulate some parking time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parkingLot.exit(LocalTime.now().plusHours(12), ticket.getTicketId());
        }
    }
}
