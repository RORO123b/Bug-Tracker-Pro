package tickets.action;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Action {
    private String action;
    private String by;
    private String to;
    private String milestone;
    private LocalDate timestamp;
    private String oldStatus;
    private String newStatus;
    private String removedDev;
}
