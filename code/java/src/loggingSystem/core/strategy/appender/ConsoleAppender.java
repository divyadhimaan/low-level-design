package loggingSystem.core.strategy.appender;

import loggingSystem.core.model.LogMessage;
import loggingSystem.core.strategy.formatter.LogFormatter;
import lombok.Getter;
import lombok.Setter;

public class ConsoleAppender implements LogAppender{
    @Getter @Setter
    private LogFormatter logFormatter;

    public ConsoleAppender(LogFormatter formatter){
        this.logFormatter = formatter;
    }

    @Override
    public void append(LogMessage logMessage){
        System.out.println(logFormatter.format(logMessage));
    }

    @Override
    public void close(){

    }
}
