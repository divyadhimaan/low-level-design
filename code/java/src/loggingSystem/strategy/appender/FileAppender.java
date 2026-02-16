package loggingSystem.strategy.appender;

import loggingSystem.model.LogMessage;
import loggingSystem.strategy.formatter.LogFormatter;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileAppender implements LogAppender{
    @Getter
    @Setter
    private LogFormatter logFormatter;
    private FileWriter fileWriter;

    public FileAppender(LogFormatter formatter, String filePath){
        this.logFormatter = formatter;
        try{
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            this.fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to initialize FileAppender for path: " + filePath, e);
        }
    }

    @Override
    public synchronized void append(LogMessage logMessage){
        try {
            fileWriter.write(logFormatter.format(logMessage));
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Failed to write logs to file, exception: "+ e.getMessage());
        }
    }

    @Override
    public void close(){
        try{
            fileWriter.close();
        }catch (IOException e) {
            System.out.println("Failed to close log files, exception: "+ e.getMessage());
        }
    }
}
