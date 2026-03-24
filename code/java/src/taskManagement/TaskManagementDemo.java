package taskManagement;

import taskManagement.enums.TaskPriority;
import taskManagement.enums.TaskStatus;
import taskManagement.model.Epic;
import taskManagement.model.Task;
import taskManagement.model.User;
import taskManagement.strategy.SortByDueDate;
import taskManagement.strategy.TaskSortStrategy;

import java.time.LocalDate;
import java.util.List;

public class TaskManagementDemo {
    public static void main(String[] args){
        TaskManagementSystem taskManagementSystem = TaskManagementSystem.getInstance();

        //Create Users
        User pm = taskManagementSystem.createUser("Product Manager", "pm@example.com")
        User user1 = taskManagementSystem.createUser("Alice", "alice@example.com");
        User user2 = taskManagementSystem.createUser("Bob", "bob@example.com");


        //Create Epics (task Lists)
        Epic epic1 = taskManagementSystem.createEpic("New features");
        Epic epic2 = taskManagementSystem.createEpic("Feedback Changes");

        //Create Tasks
        Task task1 = taskManagementSystem.createTask("Add Access Control", "Portal needs user based access control",
                LocalDate.now().plusDays(3), TaskPriority.CRITICAL, pm.getUserId());

        Task task2 = taskManagementSystem.createTask("Add Admin Page Dashboard", "Add Grafana based admin dashboard",
                LocalDate.now().plusDays(4), TaskPriority.MEDIUM, pm.getUserId());
        Task subtask = taskManagementSystem.createTask("Create Admin Page UI/UX", "Create Admin Page Design",
                LocalDate.now().plusDays(2), TaskPriority.CRITICAL, pm.getUserId());
        task2.addSubtask(subtask);

        // add task to epic(task lists)
        epic1.addTask(task1);
        epic2.addTask(task2);


        epic2.display();

        //change status of task
        task1.startProgress();

        subtask.setAssignee(user1);
        task2.setAssignee(user1);
        task1.setAssignee(user2);


        //filter task by status
        List<Task> filteredTasks = taskManagementSystem.ListTaskByStatus(TaskStatus.IN_PROGRESS);
        System.out.println("\nIn progress Tasks:");
        for (Task task : filteredTasks) {
            System.out.println(task.getTitle());
        }

        // mark task as done
        task1.completeTask();


        // search task
        List<Task> searchResults = taskManagementSystem.searchTasks("admin", new SortByDueDate());
        System.out.println("\nSearch Results:");
        for (Task task : searchResults) {
            System.out.println(task.getTitle());
        }

        // get tasks filtered by user

        List<Task> filteredTasksByUser = taskManagementSystem.ListTaskByUser(user1.getUserId());
        System.out.println("\nFiltered Results by user" + user1.getName());
        for (Task task : filteredTasksByUser) {
            System.out.println(task.getTitle());
        }

        taskManagementSystem.deleteTask(task2.getTaskId());
    }
}
