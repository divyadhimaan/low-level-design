package taskManagement.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Comment {
    private final String id;
    private final String content;
    private final User author;
    private final LocalDateTime timestamp;

    Comment(String content, User author){
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.author = author;
        this.timestamp = LocalDateTime.now();
    }
}
