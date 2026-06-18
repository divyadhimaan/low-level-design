package strategy;


public class HourlyPaymentStrategy implements PaymentStrategy{
    private static final double HOURLY_RATE = 2.0;
    @Override
    public double calculate(long duration){
        long hours = duration / 60;
        if (duration % 60 != 0) {
            hours++;
        }
        return HOURLY_RATE * hours;
    };
}
