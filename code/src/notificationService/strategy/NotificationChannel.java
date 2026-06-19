package strategy;

import model.Client;
import model.NotificationRequest;

public abstract class NotificationChannel {

    // Template method — defines the fixed sequence
    public final void sendNotification(NotificationRequest request, Client client) {
        if (!validate(request, client)) {
            System.out.println("Validation failed for request: " + request.getId());
            return;
        }
        String formatted = formatMessage(request, client);
        send(formatted, client);
        logResult(client, formatted);
    }

    // Common validation — subclasses can override to add channel-specific checks
    protected boolean validate(NotificationRequest request, Client client) {
        return request != null
                && request.getMessage() != null
                && !request.getMessage().trim().isEmpty()
                && client != null;
    }

    // Subclasses format the message for their channel
    protected abstract String formatMessage(NotificationRequest request, Client client);

    // Subclasses handle actual delivery
    protected abstract void send(String formattedMessage, Client client);

    // Common logging — subclasses can override if needed
    protected void logResult(Client client, String formattedMessage) {
        System.out.printf("[%s] ✓ Delivered to %s | Message: %s%n",
                this.getClass().getSimpleName(), client.getName(), formattedMessage);
    }
}
