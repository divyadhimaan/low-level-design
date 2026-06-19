package strategy;

import model.RateLimiterConfig;

public class LeakyBucketRateLimiter implements RateLimiterStrategy {

    public LeakyBucketRateLimiter(RateLimiterConfig config) {
    }

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        // TODO: implement leaky bucket logic
        return false;
    }
}
