package loggingSystem.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogMessage {
    private final LogLevel logLevel;
    private final String message;
    private final LocalDateTime timestamp;
    private final String loggerName;

    public LogMessage(LogLevel logLevel, String loggerName, String message){
        this.logLevel = logLevel;
        this.message = message;
        this.loggerName = loggerName;
        this.timestamp = LocalDateTime.now();
    }
}
