package Tickets;

import enums.BusinessValue;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UIFeedback extends Ticket{
    protected String uiElementId;
    protected BusinessValue businessValue;
    protected Integer usabilityScore;
    protected String screenshotUrl;
    protected String suggestedFix;

    public UIFeedback() {
        super();
        type = TicketType.UI_FEEDBACK;
        status = TicketStatus.OPEN;
    }
}
