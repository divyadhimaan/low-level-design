package taskManagement.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private final String userId;
    private final String email;
    private final String name;

    public User(String email, String name){
        userId = UUID.randomUUID().toString();
        this.email = email;
        this.name = name;
    }

}
