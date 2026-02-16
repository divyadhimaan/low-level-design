package loggingSystem.strategy.formatter;

import loggingSystem.model.LogMessage;

import java.time.format.DateTimeFormatter;

public class TextFormatter implements LogFormatter{
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    @Override
    public String format(LogMessage message){
        return String.format("%s %s - %s: %s\n",
                message.getTimestamp().format(DATE_TIME_FORMATTER),
                message.getLogLevel(),
                message.getLoggerName(),
                message.getMessage());
    }
}
