package taskManagement.model;

import java.time.LocalDateTime;

public class TaskActivityLog {
    private final LocalDateTime timestamp;
    private final String description;

    public TaskActivityLog(String description){
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    @Override
    public String toString(){
        return "[" + timestamp + "]" + description;
    }
}
