package service;

import model.ParkingLotTicket;
import strategy.PaymentStrategy;

import java.time.LocalTime;

public class PaymentService {

    public double calculatePaymentAmount(ParkingLotTicket ticket, PaymentStrategy strategy, LocalTime exitTime) {
        long duration = Math.abs(java.time.Duration.between(exitTime, ticket.getEntryTime()).toMinutes());
        System.out.println(duration);
        double amount = strategy.calculate(duration);
        // Process payment with the calculated amount
        System.out.println("Payment of $" + amount + " initiated for ticket ID: " + ticket.getTicketId());
        return amount;
    }

    public boolean processPayment(Double amount, ParkingLotTicket ticket) {
        // Here you would integrate with a payment gateway to process the payment
        // For this example, we'll assume the payment is always successful
        System.out.println("Payment of $" + amount + " processed successfully for ticket ID: " + ticket.getTicketId());
        return true;
    }
}


