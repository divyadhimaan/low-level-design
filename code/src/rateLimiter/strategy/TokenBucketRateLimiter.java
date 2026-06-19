package strategy;

import model.RateLimiterConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements RateLimiterStrategy {

    private final Map<String, Double> tokenCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRefillTimes = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimiterConfig config) {}

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        long currTime = System.currentTimeMillis();
        long lastRefillTime = lastRefillTimes.getOrDefault(clientId, currTime);

        double currentTokens = tokenCounts.getOrDefault(clientId, config.getBucketCapacity());
        double tokensToAdd = (currTime - lastRefillTime) * config.getRefillRate() / 1000.0;
        currentTokens = Math.min(config.getBucketCapacity(), currentTokens + tokensToAdd);

        lastRefillTimes.put(clientId, currTime);

        if (currentTokens >= 1) {
            tokenCounts.put(clientId, currentTokens - 1);
            return true;
        } else {
            tokenCounts.put(clientId, currentTokens);
            return false;
        }
    }
}
