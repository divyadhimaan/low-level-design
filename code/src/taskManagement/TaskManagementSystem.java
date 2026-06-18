package taskManagement;

import taskManagement.enums.TaskPriority;
import taskManagement.enums.TaskStatus;
import taskManagement.model.Task;
import taskManagement.model.Epic;
import taskManagement.model.User;
import taskManagement.strategy.TaskSortStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManagementSystem {
    private static TaskManagementSystem instance;
    private final Map<String, User> users;
    private final Map<String, Epic> epicList;
    private final Map<String, Task> tasks;

    private TaskManagementSystem(){
        users = new ConcurrentHashMap<>();
        epicList = new ConcurrentHashMap<>();
        tasks = new ConcurrentHashMap<>();
    }

    public static synchronized TaskManagementSystem getInstance(){
        if(instance == null){
            instance = new TaskManagementSystem();
        }
        return instance;
    }

    public User createUser(String name, String email){
        User user = new User(name, email);
        users.put(user.getUserId(), user);
        return user;
    }

    public Epic createEpic(String name){
        Epic epic = new Epic(name);
        epicList.put(epic.getId(), epic);
        return epic;
    }

    public Task createTask(String title, String description, LocalDate dueDate,
                           TaskPriority priority, String createdByUserId){
        User createdBy = users.get(createdByUserId);

        if(createdBy == null){
            throw new IllegalArgumentException("Invalid User");
        }

        Task task = new Task.TaskBuilder(title)
                .description(description)
                .dueDate(dueDate)
                .priority(priority)
                .createdBy(createdBy)
                .build();

        tasks.put(task.getTaskId(), task);
        return task;
    }

    public void deleteTask(String taskId){
        tasks.remove(taskId);
    }

    public List<Task> ListTaskByUser(String userId){
        User user = users.get(userId);
        if(user == null){
            throw new IllegalArgumentException("Invalid User");
        }

        return tasks.values().stream()
                .filter(task -> user.equals(task.getAssignee()))
                .collect(Collectors.toList());
    }

    public List<Task> ListTaskByStatus(TaskStatus status){
        return tasks.values().stream()
                .filter(task -> task.getCurrentState().getStatus() == status)
                .collect(Collectors.toList());

    }
    public List<Task> searchTasks(String keyword, TaskSortStrategy sortStrategy){
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedKeyword = keyword.toLowerCase();

        List<Task> matchingTasks = tasks.values().stream()
                .filter( task -> {
                    String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";
                    String description = task.getDescription() != null ? task.getDescription().toLowerCase() : "";

                    return title.contains(normalizedKeyword) || description.contains(normalizedKeyword);
                })
                .collect(Collectors.toList());

        sortStrategy.sort(matchingTasks);
        return matchingTasks;
    }
}
