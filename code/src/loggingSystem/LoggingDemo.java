package loggingSystem;

import loggingSystem.core.model.LogLevel;
import loggingSystem.core.Logger;
import loggingSystem.core.LoggingManager;
import loggingSystem.core.strategy.appender.ConsoleAppender;
import loggingSystem.core.strategy.appender.FileAppender;
import loggingSystem.core.strategy.formatter.TextFormatter;

public class LoggingDemo {
    public static void main(String[] args){
        LoggingManager loggingManager = LoggingManager.getInstance();
        Logger rootLogger = loggingManager.getRootLogger();
        rootLogger.setLogLevel(LogLevel.INFO);


        rootLogger.addAppender(new ConsoleAppender(new TextFormatter()));

        Logger mainLogger = loggingManager.getLogger("com.example.main");
        mainLogger.info("Application Starting");
        mainLogger.debug("this is debug message");
        mainLogger.warn("This is warning message");

        Logger dbLogger = loggingManager.getLogger("com.example.db");
        dbLogger.setLogLevel(LogLevel.DEBUG);
        dbLogger.info("DB service");
        dbLogger.debug("This is debug message");


        Logger serviceLogger = loggingManager.getLogger("com.example.service");
        serviceLogger.addAppender(new FileAppender(new TextFormatter(), "code/java/src/loggingSystem/logs/app.txt"));

        serviceLogger.info("Service starting");
        serviceLogger.warn("This is warning log");


        loggingManager.shutdown();
    }
}
