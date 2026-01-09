package tickets;

import enums.BusinessValue;
import enums.CustomerDemand;

public final class FeatureRequestBuilder
        extends TicketBuilder<FeatureRequest, FeatureRequestBuilder> {

    public FeatureRequestBuilder() {
        this.ticket = new FeatureRequest();
    }

    @Override
    protected FeatureRequestBuilder self() {
        return this;
    }

    /**
     * @param value the business value of the feature request
     * @return this builder instance
     */
    public FeatureRequestBuilder businessValue(final BusinessValue value) {
        ticket.setBusinessValue(value);
        return this;
    }

    /**
     * @param demand the customer demand level
     * @return this builder instance
     */
    public FeatureRequestBuilder customerDemand(final CustomerDemand demand) {
        ticket.setCustomerDemand(demand);
        return this;
    }

    @Override
    public FeatureRequest build() {
        return ticket;
    }
}
