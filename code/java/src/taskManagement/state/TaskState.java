package taskManagement.state;

import taskManagement.enums.TaskStatus;
import taskManagement.model.Task;

public interface TaskState {
    void startProgress(Task task);
    void completeTask(Task task);
    void reopenTask(Task task);
    TaskStatus getStatus();
}
