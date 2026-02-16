package loggingSystem.orchestrator;

import loggingSystem.model.LogLevel;
import loggingSystem.model.LogMessage;
import loggingSystem.strategy.appender.LogAppender;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logger {
    private final String name;
    @Setter
    private LogLevel logLevel;
    private final Logger parent;
    @Getter
    private final List<LogAppender> appenderList;

    Logger(String name, Logger parent){
        this.name = name;
        this.parent = parent;
        this.appenderList = new CopyOnWriteArrayList<>();
    }

    public void addAppender(LogAppender appender){
        appenderList.add(appender);
    }

    public LogLevel getEffectiveLevel(){
        for(Logger logger = this; logger != null; logger = logger.parent){
            LogLevel currLevel = logger.logLevel;
            if(currLevel != null){
                return currLevel;
            }
        }
        return LogLevel.DEBUG;
    }

    public void log(LogLevel logLevel, String message){
        if(logLevel.isGreaterOrEqual(getEffectiveLevel())) {
            LogMessage logMessage = new LogMessage(logLevel, this.name, message);
            callAppender(logMessage);
        }
    }
    private void callAppender(LogMessage message){

        Logger current = this;

        while(current != null){
            if(!current.appenderList.isEmpty()) {
                LoggingManager.getInstance().getProcessor().process(message, current.appenderList);
            }
            current = current.parent;
        }
    }

    public void debug(String message){
        log(LogLevel.DEBUG, message);
    }

    public void info(String message){
        log(LogLevel.INFO, message);
    }

    public void warn(String message){
        log(LogLevel.WARN, message);
    }

    public void error(String message){
        log(LogLevel.ERROR, message);
    }
}
