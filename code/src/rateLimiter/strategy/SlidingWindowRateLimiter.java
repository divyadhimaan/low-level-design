package strategy;

import model.RateLimiterConfig;

public class SlidingWindowRateLimiter implements RateLimiterStrategy {

    public SlidingWindowRateLimiter(RateLimiterConfig config) {
    }

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        // TODO: implement sliding window logic
        return false;
    }
}
