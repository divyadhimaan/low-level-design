package loggingSystem.strategy.appender;

import loggingSystem.model.LogMessage;

public interface LogAppender {
    void append(LogMessage message);
    void close();
}
