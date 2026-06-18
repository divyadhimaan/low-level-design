package taskManagement.state;

import taskManagement.enums.TaskStatus;
import taskManagement.model.Task;

public class InProgressState implements TaskState{
    @Override
    public void startProgress(Task task){
        System.out.println("Task is already in progress state.");
    }

    @Override
    public void completeTask(Task task){
        task.setState(new DoneState());
    }

    @Override
    public void reopenTask(Task task){
        task.setState(new ToDoState());
    }

    @Override
    public TaskStatus getStatus(){
        return TaskStatus.IN_PROGRESS;
    }

}
