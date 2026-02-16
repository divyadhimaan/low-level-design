package loggingSystem.strategy.formatter;

import loggingSystem.model.LogMessage;

public interface LogFormatter {
    String format(LogMessage message);
}
