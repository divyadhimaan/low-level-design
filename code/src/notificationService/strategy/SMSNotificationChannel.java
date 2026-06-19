package strategy;

import model.Client;
import model.NotificationRequest;

public class SMSNotificationChannel extends NotificationChannel {

    @Override
    protected String formatMessage(NotificationRequest request, Client client) {
        String message = request.getMessage();
        if (message.length() > 160) {
            message = message.substring(0, 157) + "...";
        }
        return message;
    }

    @Override
    protected void send(String formattedMessage, Client client) {
        // Integrate with SMS gateway (e.g. Twilio)
    }
}
