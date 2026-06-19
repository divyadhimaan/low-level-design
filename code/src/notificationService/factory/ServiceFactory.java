package factory;

import model.Service;
import strategy.NotificationChannel;

import java.util.List;
import java.util.UUID;

public class ServiceFactory {

    public static Service createService(String name, List<Class<? extends NotificationChannel>> allowedChannels) {
        return new Service(UUID.randomUUID().toString(), name, allowedChannels);
    }
}
