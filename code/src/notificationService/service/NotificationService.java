package service;

import model.Client;
import model.NotificationRequest;
import model.Service;
import repository.SubscriptionRepository;
import strategy.NotificationChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class NotificationService {

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 1000;

    private final SubscriptionRepository subscriptionRepository;
    private final ExecutorService executor;
    private final Queue<NotificationRequest> dlq = new ConcurrentLinkedQueue<>();

    public NotificationService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.executor = Executors.newCachedThreadPool();
    }

    public void processNotification(NotificationRequest request, Service service) {
        List<Client> subscribedClients = subscriptionRepository.getSubscribedClients(service);

        List<Future<?>> futures = new ArrayList<>();

        for (Client client : subscribedClients) {
            List<Class<? extends NotificationChannel>> channels =
                    subscriptionRepository.getSubscribedChannels(service, client);

            for (Class<? extends NotificationChannel> channelClass : channels) {
                futures.add(executor.submit(() -> sendWithRetry(request, client, channelClass)));
            }
        }

        // Wait for all deliveries to complete before returning
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                System.out.println("Unexpected error during delivery: " + e.getCause().getMessage());
            }
        }
    }

    private void sendWithRetry(NotificationRequest request, Client client,
                               Class<? extends NotificationChannel> channelClass) {
        int attempts = 0;
        long backoff = INITIAL_BACKOFF_MS;

        while (attempts < MAX_RETRIES) {
            try {
                NotificationChannel channel = channelClass.getDeclaredConstructor().newInstance();
                channel.sendNotification(request, client);
                return; // success
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed for " + channelClass.getSimpleName()
                        + " -> client " + client.getName() + ": " + e.getMessage());

                if (attempts < MAX_RETRIES) {
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    backoff *= 2;
                }
            }
        }

        addToDLQ(request);
    }

    private void addToDLQ(NotificationRequest request) {
        System.out.println("All retries exhausted. Moving to DLQ: " + request.getId());
        dlq.add(request);
    }

    public Queue<NotificationRequest> getDLQ() {
        return dlq;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
