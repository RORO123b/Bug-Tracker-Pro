package tickets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private String author;
    private String content;
    private String createdAt;
}
