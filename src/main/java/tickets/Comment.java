package tickets;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Comment {
    private String author;
    private String content;
    private LocalDate createdAt;

    public Comment(String author, String content, LocalDate createdAt) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }
}
