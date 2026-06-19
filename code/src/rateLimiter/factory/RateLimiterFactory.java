package factory;

import model.RateLimiterConfig;
import model.Strategy;
import strategy.*;

public class RateLimiterFactory {
    public RateLimiterStrategy create(Strategy strategy, RateLimiterConfig config) {
        switch (strategy) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(config);
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config);
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(config);
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }
}
