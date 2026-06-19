import factory.ClientFactory;
import factory.ServiceFactory;
import model.Client;
import model.NotificationRequest;
import model.Service;
import strategy.EmailNotificationChannel;
import strategy.PushNotificationChannel;
import strategy.SMSNotificationChannel;

import java.util.List;

public class NotificationSystemDemo {
    public static void main(String[] args){

        // Create new client
        Client client1 = ClientFactory.createClient("Alice", "alice@gmail.com", "1234567890", "deviceToken123");
        Client client2 = ClientFactory.createClient("Ben", "ben@gmail.com", "1234237890", "deviceToken1456");

        //Create services
         Service service1 = ServiceFactory.createService("Weather Updates", List.of(EmailNotificationChannel.class, SMSNotificationChannel.class));
         Service service2 = ServiceFactory.createService("Stock Alerts", List.of(EmailNotificationChannel.class, PushNotificationChannel.class));


        NotificationSystem system = NotificationSystem.getInstance();

        // Register clients and services
        system.registerClient(client1);
        system.registerClient(client2);
        system.registerService(service1);
        system.registerService(service2);

        system.subscribe(service1, client1, EmailNotificationChannel.class);
        system.subscribe(service1, client1, SMSNotificationChannel.class);
        system.subscribe(service1, client2, EmailNotificationChannel.class);
        system.subscribe(service2, client2, PushNotificationChannel.class);


        NotificationRequest request1 = new NotificationRequest.Builder()
                .setServiceId(service1.getId())
                .setMessage("It's going to rain today!")
                .build();

        NotificationRequest request2 = new NotificationRequest.Builder()
                .setServiceId(service2.getId())
                .setMessage("AAPL stock price just hit $150!")
                .build();

        // Send notifications
        system.sendNotification(request1);
        system.sendNotification(request2);
    }
}
