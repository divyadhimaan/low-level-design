package loggingSystem.core.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogMessage {
    private final LogLevel logLevel;
    private final String message;
    private final LocalDateTime timestamp;
    private final String loggerName;

    public LogMessage(Builder builder){
        this.logLevel = builder.logLevel;
        this.message = builder.message;
        this.loggerName = builder.loggerName;
        this.timestamp = builder.timestamp;
    }

    public static class Builder{
        private LocalDateTime timestamp = LocalDateTime.now();
        private LogLevel logLevel;
        private String message;
        private String loggerName;

        public Builder logLevel(LogLevel level){
            this.logLevel = level;
            return this;
        }

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp){
            this.timestamp = timestamp;
            return this;
        }

        public Builder logger(String loggerName){
            this.loggerName = loggerName;
            return this;
        }

        public LogMessage build() {
            if (logLevel == null) {
                throw new IllegalStateException("LogLevel is required");
            }
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalStateException("Message is required");
            }
            return new LogMessage(this);
        }

    }
}
