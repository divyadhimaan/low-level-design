public class RateLimiterConfig {
    private final int maxRequests;      // requests allowed per window
    private final long windowSizeMs;    // window duration in ms
    private final int bucketCapacity;   // for token/leaky bucket

    private RateLimiterConfig(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.windowSizeMs = builder.windowSizeMs;
        this.bucketCapacity = builder.bucketCapacity;
    }

    public static class Builder{

        private int maxRequests = 100;      // default
        private long windowSizeMs = 60000;  // default 1 minute
        private int bucketCapacity = 100;   // default

        public Builder setMaxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder setWindowSizeMs(long windowSizeMs) {
            this.windowSizeMs = windowSizeMs;
            return this;
        }

        public Builder setBucketCapacity(int bucketCapacity) {
            this.bucketCapacity = bucketCapacity;
            return this;
        }

        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
}
