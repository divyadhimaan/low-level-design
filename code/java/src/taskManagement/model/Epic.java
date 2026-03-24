package taskManagement.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public class Epic {
    @Getter
    private final String name;
    @Getter
    private final String id;
    private final List<Task> tasks;

    public Epic(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.tasks = new CopyOnWriteArrayList<>();
    }

    public void addTask(Task task){
        this.tasks.add(task);
    }

    public List<Task> getTasks(){
        return new ArrayList<>(tasks);
    }

    public void display(){
        System.out.println("--- Task List: " + name + " ---");
        for (Task task : tasks) {
            task.display("");
        }
        System.out.println("-----------------------------------");
    }

}
