package loggingSystem.core.strategy.appender;

import loggingSystem.core.model.LogMessage;

public interface LogAppender {
    void append(LogMessage message);
    void close();
}
