package strategy;

import model.RateLimiterConfig;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements RateLimiterStrategy {



    public TokenBucketRateLimiter(RateLimiterConfig config) {}

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {

    }
}
