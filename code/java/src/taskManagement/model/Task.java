package taskManagement.model;

import lombok.Getter;
import lombok.Setter;
import taskManagement.enums.TaskPriority;
import taskManagement.state.TaskState;
import taskManagement.state.ToDoState;

import java.time.LocalDate;
import java.util.*;

@Getter
public class Task {
    private final String taskId;
    @Setter
    private String title;
    @Setter
    private String description;
    private TaskPriority priority;
    private TaskState currentState;
    private LocalDate dueDate;
    private final User createdBy;
    private User assignee;
    private Set<Tag> tags;
    private List<Comment> comments;
    private List<TaskActivityLog> logs;
    private final List<Task> subTasks;

    private Task(TaskBuilder builder){
        this.taskId = builder.taskId;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.dueDate = builder.dueDate;
        this.createdBy = builder.createdBy;
        this.assignee = builder.assignee;
        this.tags = builder.tags;

        this.currentState = new ToDoState();
        this.comments = new ArrayList<>();
        this.subTasks = new ArrayList<>();
        this.logs = new ArrayList<>();
        addLog("Task created with title: " + title);

    }

    public synchronized void setAssignee(User user){
        this.assignee = user;
        addLog("Assigned to " + user.getName());
    }

    public synchronized void updatePriority(TaskPriority priority) {
        this.priority = priority;
        addLog("Priority updated to " + priority.name());
    }

    public synchronized void addComment(Comment comment){
        comments.add(comment);
        addLog("Comment added by " + comment.getAuthor().getName());
    }

    public synchronized void addSubtask(Task subtask){
        subTasks.add(subtask);
        addLog("Subtask added " + subtask.getTitle());
    }

    public void setState(TaskState state){
        this.currentState = state;
        addLog("Status changed to: "+ state.getStatus());
    }

    public void startProgress(){
        currentState.startProgress(this);
    }

    public void reopenTask(){
        currentState.reopenTask(this);
    }

    public void completeTask(){
        currentState.completeTask(this);
    }

    public void display(String indent) {
        System.out.println(indent + "- " + title + " [" + getCurrentState() + ", " + priority + ", Due: " + dueDate + "]");
        if (!subTasks.isEmpty()) {
            for (Task subtask : subTasks) {
                subtask.display(indent + "  ");
            }
        }
    }

    public void addLog(String log){
        this.logs.add(new TaskActivityLog(log));
    }

    public static class TaskBuilder{
        private final String taskId;
        private String title;
        private String description;
        private TaskPriority priority;
        private LocalDate dueDate;
        private User createdBy;
        private User assignee;
        private Set<Tag> tags;

        public TaskBuilder(String title){
            this.title = title;
            this.taskId = UUID.randomUUID().toString();
        }

        public TaskBuilder description(String description)
        {
            this.description = description;
            return this;
        }

        public TaskBuilder priority(TaskPriority priority)
        {
            this.priority = priority;
            return this;
        }

        public TaskBuilder dueDate(LocalDate dueDate)
        {
            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder createdBy(User createdBy)
        {
            this.createdBy = createdBy;
            return this;
        }

        public TaskBuilder assignee(User assignee)
        {
            this.assignee = assignee;
            return this;
        }

        public TaskBuilder tags(Set<Tag> tags)
        {
            this.tags = tags;
            return this;
        }

        public Task build(){
            return new Task(this);
        }
    }
}
