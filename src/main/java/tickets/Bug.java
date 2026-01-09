package tickets;

import enums.Frequency;
import enums.Severity;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bug extends Ticket {
    private String expectedBehavior;
    private String actualBehavior;
    private Frequency frequency;
    private Severity severity;
    private String environment;
    private Integer errorCode;
    Bug() {
        super();
        type = TicketType.BUG;
        status = TicketStatus.OPEN;
    }
}
