package strategy;

public class MinutePaymentStrategy implements PaymentStrategy{
    private static final double MINUTE_RATE = 0.05;  // $0.05 per minute
    @Override
    public double calculate(long duration) {
        long minutes = duration /  60;

        return MINUTE_RATE * minutes;
    }
}
