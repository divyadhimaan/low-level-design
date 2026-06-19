package strategy;

public interface RateLimiterStrategy {
    boolean isAllowed(String clientId, RateLimiterConfig config);
}
