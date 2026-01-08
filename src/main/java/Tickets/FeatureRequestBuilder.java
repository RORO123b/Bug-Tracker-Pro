package Tickets;

import enums.BusinessValue;
import enums.CustomerDemand;
import enums.TicketType;

public class FeatureRequestBuilder extends TicketBuilder<FeatureRequest, FeatureRequestBuilder> {

    public FeatureRequestBuilder() {
        this.ticket = new FeatureRequest();
    }

    @Override
    protected FeatureRequestBuilder self() {
        return this;
    }

    public FeatureRequestBuilder businessValue(BusinessValue value) {
        ticket.setBusinessValue(value);
        return this;
    }

    public FeatureRequestBuilder customerDemand(CustomerDemand demand) {
        ticket.setCustomerDemand(demand);
        return this;
    }

    @Override
    public FeatureRequest build() {
        return ticket;
    }
}
