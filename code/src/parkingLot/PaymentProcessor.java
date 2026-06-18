import strategy.PaymentStrategy;

public class PaymentProcessor {

    private PaymentStrategy paymentStrategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public double calculateStrategy(long duration) {
        return paymentStrategy.calculate(duration);
    }
}
