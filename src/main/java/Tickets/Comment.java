package Tickets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private String author;
    private String content;
    private String createdAt;
}
