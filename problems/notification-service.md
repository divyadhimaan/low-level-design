# Design Notification Service


A notification service that allows backend services to send notifications to clients via multiple channels. The system acts as a broker, routing notifications based on client subscriptions and service-allowed channels.

## Requirements


- The system should allow services to send notification requests with message content and target client details.
- The system should support multiple notification channels: Email, SMS, and Push notifications.
- Clients should be able to subscribe to specific channels on a per-service basis.
- The system should route notifications only through channels that are both allowed by the service and subscribed to by the client.
- The system should support extensibility for adding new notification channels in the future.
- The system should handle delivery failures with exponential backoff retry logic.
- Notifications that exhaust all retries should be moved to a Dead Letter Queue (DLQ) for auditing.
- The system should use a singleton central entry point for managing subscriptions and accepting notification requests.


