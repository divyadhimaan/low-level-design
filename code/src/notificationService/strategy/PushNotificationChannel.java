package strategy;

import model.Client;
import model.NotificationRequest;

public class PushNotificationChannel extends NotificationChannel {

    @Override
    protected String formatMessage(NotificationRequest request, Client client) {
        String message = request.getMessage();
        if (message.length() > 100) {
            message = message.substring(0, 97) + "...";
        }
        return message;
    }

    @Override
    protected void send(String formattedMessage, Client client) {
        // Integrate with push provider (e.g. FCM, APNs)
    }
}
