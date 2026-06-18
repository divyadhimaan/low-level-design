package taskManagement.strategy;

import taskManagement.model.Task;

import java.util.Comparator;
import java.util.List;

public class SortByPriority implements TaskSortStrategy{
    @Override
    public void sort(List<Task> taskList){
        taskList.sort(Comparator.comparing(Task::getPriority).reversed());
    }
}
