package taskManagement.strategy;

import taskManagement.model.Task;

import java.util.List;

public interface TaskSortStrategy {
    void sort(List<Task> taskList);
}
