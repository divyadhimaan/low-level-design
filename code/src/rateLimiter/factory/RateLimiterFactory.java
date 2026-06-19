package factory;

import strategy.*;

public class RateLimiterFactory {
    public RateLimiterStrategy create(Strategy strategy, RateLimiterConfig config) {
        switch (strategy) {
            case Strategy.FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
            case Strategy.SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(config);
            case Strategy.TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config);
            case Strategy.LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(config);
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }
}
