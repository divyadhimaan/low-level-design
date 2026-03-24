# 

A thread-safe task management system that supports:
- Task creation and lifecycle management
- User assignment
- Epics (task grouping)
- Subtasks (hierarchical tasks)
- Searching and filtering
- Extensible sorting and state transitions

The system is designed using OOP principles + design patterns to ensure scalability and maintainability.

## Entity Overview

| Category             | Class / Enum           | Responsibility                                                                |
|----------------------|------------------------|-------------------------------------------------------------------------------|
| **Core System**      | `TaskManagementSystem` | Central coordinator managing users, tasks, and epics (CRUD + search + filter) |
|                      | `TaskManagementDemo`   | Driver class to simulate system behavior                                      |
| **Model**            | `Task`                 | Core entity representing a task with lifecycle, subtasks, logs, and metadata  |
|                      | `Epic`                 | Groups multiple tasks (acts as task container)                                |
|                      | `User`                 | Represents a system user                                                      |
|                      | `Comment`              | Stores user comments on tasks                                                 |
|                      | `Tag`                  | Labels associated with tasks                                                  |
|                      | `TaskActivityLog`      | Tracks task history and actions                                               |
| **Enums**            | `TaskPriority`         | Defines task priority levels (LOW → CRITICAL)                                 |
|                      | `TaskStatus`           | Defines task lifecycle states (TODO, IN_PROGRESS, DONE)                       |
| **State Pattern**    | `TaskState`            | Interface defining task state behavior                                        |
|                      | `ToDoState`            | Initial task state                                                            |
|                      | `InProgressState`      | Task is actively being worked on                                              |
|                      | `DoneState`            | Task is completed                                                             |
| **Strategy Pattern** | `TaskSortStrategy`     | Interface for sorting tasks                                                   |
|                      | `SortByDueDate`        | Sort tasks by due date                                                        |
|                      | `SortByPriority`       | Sort tasks by priority                                                        |
| **Builder Pattern**  | `TaskBuilder`          | Builds Task objects with optional fields                                      |


## Design Patterns Used

| Pattern                  | Used In                                                  | Problem Solved                                    | Why This Pattern                                        |
|--------------------------|----------------------------------------------------------|---------------------------------------------------|---------------------------------------------------------|
| **Singleton**            | `TaskManagementSystem`                                   | Multiple instances causing inconsistent state     | Ensures single global instance managing all tasks/users |
| **Builder**              | `Task (TaskBuilder)`                                     | Complex object creation with many optional fields | Avoids telescoping constructors, improves readability   |
| **Strategy**             | `TaskSortStrategy`, `SortByDueDate`, `SortByPriority`    | Multiple sorting behaviors with conditional logic | Enables dynamic and extensible sorting (OCP compliant)  |
| **State**                | `TaskState`, `ToDoState`, `InProgressState`, `DoneState` | Complex lifecycle transitions using conditionals  | Encapsulates state behavior and transitions cleanly     |
| **Thread-Safety Design** | `ConcurrentHashMap`, `synchronized` methods              | Race conditions in concurrent environment         | Ensures safe updates and consistent data                |
