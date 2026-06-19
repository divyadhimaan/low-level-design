public class RateLimiterDemo {

    public static void main(String[] args) {
        RateLimiterConfig config = new RateLimiterConfig.Builder()
                .setMaxRequests(5)
                .setWindowSizeMs(1000)
                .build();
        RateLimiter rateLimiter = RateLimiter.getInstance(config, Strategy.FIXED_WINDOW);

        String clientId = "client1";

        for (int i = 0; i < 10; i++) {
            if (rateLimiter.isAllowed(clientId)) {
                System.out.println("Request " + (i + 1) + " allowed for " + clientId);
            } else {
                System.out.println("Request " + (i + 1) + " denied for " + clientId);
            }
        }
    }
}
