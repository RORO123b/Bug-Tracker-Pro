package tickets;

import enums.BusinessValue;
import enums.CustomerDemand;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureRequest extends Ticket {
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    private static final double MAX_VALUE_IMPACT = 100.0;
    private static final double MAX_VALUE_RISK = 20.0;
    private static final double MAX_VALUE_EFFICIENCY = 20.0;
    private static final double PERCENTAGE = 100.0;
    public FeatureRequest() {
        super();
        type = TicketType.FEATURE_REQUEST;
        status = TicketStatus.OPEN;
    }

    @Override
    public Double calculateImpactFinal() {
        return Math.round(Math.min(PERCENTAGE, (businessValue.getValue() * customerDemand.getValue() * PERCENTAGE) / MAX_VALUE_IMPACT) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateRiskFinal() {
        return Math.round(Math.min(PERCENTAGE, ((businessValue.getValue() + customerDemand.getValue()) * PERCENTAGE) / MAX_VALUE_RISK) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateEfficiencyFinal() {
        return Math.round(Math.min(PERCENTAGE, ((businessValue.getValue() + customerDemand.getValue()) / daysToResolve * PERCENTAGE) / MAX_VALUE_EFFICIENCY) * PERCENTAGE) / PERCENTAGE;
    }
}
