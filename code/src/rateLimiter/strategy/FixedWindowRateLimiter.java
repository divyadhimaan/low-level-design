package strategy;

import model.RateLimiterConfig;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowRateLimiter implements RateLimiterStrategy {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStartTimes = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterConfig config) {
    }

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        long currTime = System.currentTimeMillis();
        long windowStartTime = windowStartTimes.getOrDefault(clientId, currTime);

        if(currTime - windowStartTime >= config.getWindowSizeMs()){
            requestCounts.put(clientId, 1);
            windowStartTimes.put(clientId, currTime);
            return true;
        } else {
            int count = requestCounts.getOrDefault(clientId, 0);
            if(count < config.getMaxRequests()) {
                requestCounts.put(clientId, count + 1);
                return true;
            }
        }
        return false;
    }
}
