import model.Client;
import model.NotificationRequest;
import model.Service;
import repository.ClientInventory;
import repository.ServiceInventory;
import repository.SubscriptionRepository;
import service.NotificationService;
import strategy.NotificationChannel;

public class NotificationSystem {

    private static NotificationSystem instance;

    private final ClientInventory clientInventory;
    private final ServiceInventory serviceInventory;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    private NotificationSystem() {
        this.clientInventory = new ClientInventory();
        this.serviceInventory = new ServiceInventory();
        this.subscriptionRepository = new SubscriptionRepository();
        this.notificationService = new NotificationService(subscriptionRepository);
    }

    public static synchronized NotificationSystem getInstance() {
        if (instance == null) {
            instance = new NotificationSystem();
        }
        return instance;
    }

    // --- Registration ---

    public void registerClient(Client client) {
        clientInventory.addClient(client);
    }

    public void registerService(Service service) {
        serviceInventory.addService(service);
    }

    // --- Subscription management ---

    public void subscribe(Service service, Client client, Class<? extends NotificationChannel> channel) {
        if (!service.getAllowedChannels().contains(channel)) {
            throw new IllegalArgumentException(
                    "Channel " + channel.getSimpleName() + " is not allowed by service " + service.getName());
        }
        subscriptionRepository.subscribe(service, client, channel);
    }

    public void unsubscribe(Service service, Client client, Class<? extends NotificationChannel> channel) {
        subscriptionRepository.unsubscribe(service, client, channel);
    }

    // --- Notification entry point ---

    public void sendNotification(NotificationRequest request) {
        Service service = serviceInventory.getService(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown service: " + request.getServiceId()));

        notificationService.processNotification(request, service);
    }
}
