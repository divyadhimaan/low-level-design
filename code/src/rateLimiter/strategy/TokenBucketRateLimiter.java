package strategy;

public class TokenBucketRateLimiter implements RateLimiterStrategy{
    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        // Implement fixed window rate limiting logic here
        return false;
    }
}
