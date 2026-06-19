package repository;

import model.Client;
import model.Service;
import strategy.NotificationChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionRepository {

    // Map<serviceId, Map<clientId, List<channel type>>>
    private final Map<String, Map<String, List<Class<? extends NotificationChannel>>>> subscriptions
            = new ConcurrentHashMap<>();

    // Separate map to resolve clientId -> Client object
    private final Map<String, Client> clientRegistry = new ConcurrentHashMap<>();

    public synchronized void subscribe(Service service, Client client, Class<? extends NotificationChannel> channel) {
        clientRegistry.put(client.getId(), client);
        subscriptions
                .computeIfAbsent(service.getId(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(client.getId(), k -> new ArrayList<>())
                .add(channel);
    }

    public void unsubscribe(Service service, Client client, Class<? extends NotificationChannel> channel) {
        Map<String, List<Class<? extends NotificationChannel>>> clientMap = subscriptions.get(service.getId());
        if (clientMap != null) {
            List<Class<? extends NotificationChannel>> channels = clientMap.get(client.getId());
            if (channels != null) {
                channels.remove(channel);
            }
        }
    }

    // Returns all clients subscribed to a service
    public List<Client> getSubscribedClients(Service service) {
        Map<String, List<Class<? extends NotificationChannel>>> clientMap =
                subscriptions.getOrDefault(service.getId(), Collections.emptyMap());

        List<Client> clients = new ArrayList<>();
        for (String clientId : clientMap.keySet()) {
            Client client = clientRegistry.get(clientId);
            if (client != null) clients.add(client);
        }
        return clients;
    }

    // Returns channels a client is subscribed to for a given service
    public List<Class<? extends NotificationChannel>> getSubscribedChannels(Service service, Client client) {
        return subscriptions
                .getOrDefault(service.getId(), Collections.emptyMap())
                .getOrDefault(client.getId(), Collections.emptyList());
    }
}
