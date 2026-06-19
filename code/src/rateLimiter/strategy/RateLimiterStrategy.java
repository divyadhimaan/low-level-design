package strategy;

import model.RateLimiterConfig;

public interface RateLimiterStrategy {
    boolean isAllowed(String clientId, RateLimiterConfig config);
}
