package model;

import lombok.Getter;

@Getter
public class RateLimiterConfig {
    private final int maxRequests;      // requests allowed per window
    private final long windowSizeMs;    // window duration in ms
    private final double bucketCapacity;   // for token/leaky bucket
    private final double refillRate;        // tokens added per second

    private RateLimiterConfig(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.windowSizeMs = builder.windowSizeMs;
        this.bucketCapacity = builder.bucketCapacity;
        this.refillRate = builder.bucketCapacity / (builder.windowSizeMs / 1000); // tokens per second
    }

    public static class Builder {

        private int maxRequests = 100;      // default
        private long windowSizeMs = 60000;  // default 1 minute
        private double bucketCapacity = 100;   // default
        private double refillRate = 100;        // default

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

        public Builder setRefillRate(int refillRate) {
            this.refillRate = refillRate;
            return this;
        }

        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
}
