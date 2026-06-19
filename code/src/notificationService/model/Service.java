package model;

import lombok.Getter;
import strategy.NotificationChannel;

import java.util.List;

@Getter
public class Service {
    private final String id;
    private final String name;
    private final List<Class<? extends NotificationChannel>> allowedChannels;

    public Service(String id, String name, List<Class<? extends NotificationChannel>> allowedChannels) {
        this.id = id;
        this.name = name;
        this.allowedChannels = allowedChannels;
    }
}
