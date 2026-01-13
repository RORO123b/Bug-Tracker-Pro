package tickets;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Comment {
    private final String author;
    private final String content;
    private final LocalDate createdAt;

    public Comment(final String author, final String content, final LocalDate createdAt) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }
}
