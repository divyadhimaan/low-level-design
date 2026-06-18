package taskManagement;

import taskManagement.enums.TaskPriority;
import taskManagement.enums.TaskStatus;
import taskManagement.model.*;
import taskManagement.strategy.SortByDueDate;
import taskManagement.strategy.SortByPriority;
import taskManagement.strategy.TaskSortStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TaskManagementDemo {
    public static void main(String[] args){
        TaskManagementSystem system = TaskManagementSystem.getInstance();

        System.out.println("\n================= USER CREATION =================");
        User pm = system.createUser("Product Manager", "pm@example.com");
        User alice = system.createUser("Alice", "alice@example.com");
        User bob = system.createUser("Bob", "bob@example.com");


        System.out.println("\n================= EPIC CREATION =================");
        Epic backendEpic = system.createEpic("Backend Revamp");
        Epic uiEpic = system.createEpic("UI Improvements");


        System.out.println("\n================= TASK CREATION =================");

        Task task1 = system.createTask(
                "Auth System",
                "Implement JWT based authentication",
                LocalDate.now().plusDays(3),
                TaskPriority.CRITICAL,
                pm.getUserId()
        );

        Task task2 = system.createTask(
                "Dashboard",
                "Create admin dashboard",
                LocalDate.now().plusDays(5),
                TaskPriority.HIGH,
                pm.getUserId()
        );

        Task subtask1 = system.createTask(
                "Design Dashboard UI",
                "Create Figma designs",
                LocalDate.now().plusDays(2),
                TaskPriority.MEDIUM,
                pm.getUserId()
        );

        Task subtask2 = system.createTask(
                "API Integration",
                "Connect dashboard with backend APIs",
                LocalDate.now().plusDays(4),
                TaskPriority.HIGH,
                pm.getUserId()
        );

        // Add subtasks
        task2.addSubtask(subtask1);
        task2.addSubtask(subtask2);


        System.out.println("\n================= TAGS & COMMENTS =================");

        Tag backendTag = new Tag("backend");
        Tag urgentTag = new Tag("urgent");

        task1.updatePriority(TaskPriority.CRITICAL);
        task1.setAssignee(bob);

        task1.addComment(new Comment("Start ASAP", pm));
        task1.addComment(new Comment("Working on it", bob));

        task1.addTag(backendTag);
        task1.addTag(urgentTag);

        System.out.println("\n================= EPIC MAPPING =================");

        backendEpic.addTask(task1);
        uiEpic.addTask(task2);

        backendEpic.display();
        uiEpic.display();


        System.out.println("\n================= STATE TRANSITIONS =================");

        // Valid flow
        task1.startProgress();
        task1.completeTask();

        // Edge case: invalid transition
        task1.startProgress(); // should print error

        // Reopen flow
        task1.reopenTask();
        task1.startProgress();


        System.out.println("\n================= ASSIGNMENTS =================");

        subtask1.setAssignee(alice);
        subtask2.setAssignee(bob);
        task2.setAssignee(alice);


        System.out.println("\n================= FILTER BY STATUS =================");

        List<Task> inProgressTasks = system.ListTaskByStatus(TaskStatus.IN_PROGRESS);
        inProgressTasks.forEach(t -> System.out.println(t.getTitle()));


        System.out.println("\n================= FILTER BY USER =================");

        List<Task> aliceTasks = system.ListTaskByUser(alice.getUserId());
        aliceTasks.forEach(t -> System.out.println(t.getTitle()));


        System.out.println("\n================= SEARCH + SORT =================");

        List<Task> searchByDueDate = system.searchTasks("dashboard", new SortByDueDate());
        System.out.println("Sorted by Due Date:");
        searchByDueDate.forEach(t -> System.out.println(t.getTitle()));

        List<Task> searchByPriority = system.searchTasks("dashboard", new SortByPriority());
        System.out.println("\nSorted by Priority:");
        searchByPriority.forEach(t -> System.out.println(t.getTitle()));


        System.out.println("\n================= DELETE TASK =================");

        system.deleteTask(subtask2.getTaskId());
        System.out.println("Deleted subtask: " + subtask2.getTitle());


        System.out.println("\n================= FINAL EPIC VIEW =================");

        uiEpic.display();


        System.out.println("\n================= DEMO COMPLETE =================");
    }
}
