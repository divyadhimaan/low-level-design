package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class NotificationRequest {
    private final String id;
    private final String serviceId;
    private final String message;
    private final LocalDateTime timestamp;

    private NotificationRequest(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.serviceId = builder.serviceId;
        this.message = builder.message;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
    }

    public static class Builder {
        private String serviceId;
        private String message;
        private LocalDateTime timestamp;

        public Builder setServiceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public NotificationRequest build() {
            return new NotificationRequest(this);
        }
    }
}
