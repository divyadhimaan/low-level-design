package loggingSystem.core;

import loggingSystem.core.strategy.appender.LogAppender;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggingManager {
    private static volatile LoggingManager instance;
    private final Map<String, Logger> loggers;
    @Getter
    private final Logger rootLogger;
    @Getter
    private final AsyncLogProcessor processor;


    private LoggingManager(){
        this.loggers =  new ConcurrentHashMap<>();
        this.rootLogger = new Logger("root", null);
        this.loggers.put("root", rootLogger);
        this.processor = new AsyncLogProcessor();
    }

    public static LoggingManager getInstance(){
        if(instance == null){
            synchronized (LoggingManager.class){
                if(instance == null){
                    instance = new LoggingManager();
                }
            }
        }
        return instance;
    }

    public Logger getLogger(String name){
        Logger existing = loggers.get(name);
        if (existing != null) {
            return existing;
        }

        synchronized (this) {

            // Double check
            existing = loggers.get(name);
            if (existing != null) {
                return existing;
            }

            Logger newLogger = createLogger(name);
            loggers.put(name, newLogger);
            return newLogger;
        }
    }

    private Logger createLogger(String name){
        if(name.equals("root"))
            return rootLogger;

        int lastDot = name.lastIndexOf('.');
        String parentName = (lastDot == -1) ? "root" : name.substring(0, lastDot);
        Logger parent = getLogger(parentName);
        return new Logger(name, parent);
    }

    public void shutdown(){

        processor.stop();

        loggers.values().stream()
                .flatMap(logger -> logger.getAppenderList().stream())
                .distinct()
                .forEach(LogAppender::close);

        System.out.println("Logging Framework shut down gracefully");
    }


}
