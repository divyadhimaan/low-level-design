package taskManagement.model;

import lombok.Getter;

import java.util.UUID;

public class Tag {
    private final String id;
    @Getter
    private final String name;

    public Tag(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }


}
