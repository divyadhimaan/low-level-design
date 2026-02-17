package loggingSystem.core.strategy.formatter;

import loggingSystem.core.model.LogMessage;

public interface LogFormatter {
    String format(LogMessage message);
}
