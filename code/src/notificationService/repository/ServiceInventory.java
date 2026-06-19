package repository;

import model.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceInventory {

    private final Map<String, Service> services = new ConcurrentHashMap<>();

    public void addService(Service service) {
        services.put(service.getId(), service);
    }

    public Optional<Service> getService(String serviceId) {
        return Optional.ofNullable(services.get(serviceId));
    }

    public Collection<Service> getAllServices() {
        return services.values();
    }

    public void removeService(String serviceId) {
        services.remove(serviceId);
    }
}
