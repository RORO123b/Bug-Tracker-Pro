package Tickets;

import enums.BusinessValue;
import enums.CustomerDemand;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureRequest extends Ticket{
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    public FeatureRequest() {
        super();
        type = TicketType.FEATURE_REQUEST;
        status = TicketStatus.OPEN;
    }
}
