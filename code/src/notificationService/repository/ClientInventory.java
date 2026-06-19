package repository;

import model.Client;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ClientInventory {

    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    public void addClient(Client client) {
        clients.put(client.getId(), client);
    }

    public Optional<Client> getClient(String clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }

    public Collection<Client> getAllClients() {
        return clients.values();
    }

    public void removeClient(String clientId) {
        clients.remove(clientId);
    }
}
