package strategy;

import model.RateLimiterConfig;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowRateLimiter implements RateLimiterStrategy {

    private final Map<String, Deque<Long>> requestLogs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterConfig config) {}

    @Override
    public boolean isAllowed(String clientId, RateLimiterConfig config) {
        long currTime = System.currentTimeMillis();
        long windowStart = currTime - config.getWindowSizeMs();

        Deque<Long> log = requestLogs.computeIfAbsent(clientId, k -> new ArrayDeque<>());

        // Remove timestamps that have fallen outside the current window
        while (!log.isEmpty() && log.peekFirst() <= windowStart) {
            log.pollFirst();
        }

        if (log.size() < config.getMaxRequests()) {
            log.addLast(currTime);
            return true;
        }

        return false;
    }
}
