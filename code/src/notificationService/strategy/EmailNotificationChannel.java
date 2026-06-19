package strategy;

import model.Client;
import model.NotificationRequest;

public class EmailNotificationChannel extends NotificationChannel {

    @Override
    protected String formatMessage(NotificationRequest request, Client client) {
        return "<html><body><p>" + request.getMessage() + "</p></body></html>";
    }

    @Override
    protected void send(String formattedMessage, Client client) {
        // Integrate with email gateway (e.g. SendGrid, SES)
    }
}
