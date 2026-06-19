# Rate Limiter 



## Problem

Design and implement a Rate Limiter that controls how many requests a client can make to a system within a given time window. If a client exceeds the allowed limit, further requests should be rejected until the window resets or tokens replenish.

## Requirements: Functional


- Accept or reject a request for a given clientId based on the configured limit
- Support multiple clients simultaneously (each tracked independently)
- Support pluggable rate limiting algorithms (Strategy pattern)
- Support the following algorithms:
  - Fixed Window — allow up to N requests per fixed time window
  - Sliding Window Log — allow up to N requests in any rolling time window
  - Token Bucket — allow bursts up to capacity; refill tokens at a steady rate
  - Leaky Bucket — process requests at a constant rate; drop excess





## Requirements: Non-Functional


- Thread-safe — must handle concurrent requests correctly
- Singleton RateLimiter — single instance shared across the application
- Configurable — limit, window size, and bucket capacity set at startup via a Builder

[Java Implementation](./../code/src/rateLimiter/RateLimiterDemo.java) | [Design Explanation](./../code/src/rateLimiter/RateLimiter.md)
