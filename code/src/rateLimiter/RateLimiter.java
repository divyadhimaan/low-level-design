import factory.RateLimiterFactory;
import strategy.RateLimiterStrategy;

public class RateLimiter {
    private static RateLimiter instance;
    private RateLimiterConfig rateLimiterConfig;
    private RateLimiterStrategy rateLimiterStrategy;
    private RateLimiterFactory factory;


    private RateLimiter(RateLimiterConfig config, Strategy strategy) {
        this.rateLimiterConfig = config;
        this.rateLimiterStrategy = factory.create(strategy, config);
    }

    public static synchronized RateLimiter getInstance(RateLimiterConfig config, Strategy limitingStrategy) {
        if (instance == null) {
            instance = new RateLimiter(config,limitingStrategy);
        }
        return instance;
    }

    public boolean isAllowed(String clientId) {
        return rateLimiterStrategy.isAllowed(clientId, rateLimiterConfig);
    }




}
