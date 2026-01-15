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

    private final static double MAX_VALUE_IMPACT = 48.0;
    private final static double MAX_VALUE_RISK = 12.0;
    private final static double MAX_VALUE_EFFICIENCY = 70.0;
    private final static double PERCENTAGE = 100.0;
    Bug() {
        super();
        type = TicketType.BUG;
        status = TicketStatus.OPEN;
    }

    @Override
    public Double calculateImpactFinal() {
        return Math.round(Math.min(PERCENTAGE, (frequency.getValue() * businessPriority.getValue() * severity.getValue() * PERCENTAGE) / MAX_VALUE_IMPACT) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateRiskFinal() {
        return Math.round(Math.min(PERCENTAGE, (frequency.getValue() * severity.getValue() * PERCENTAGE) / MAX_VALUE_RISK) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateEfficiencyFinal() {
        return Math.round(Math.min(PERCENTAGE, (frequency.getValue() + severity.getValue()) * 10.0 / daysToResolve * PERCENTAGE / MAX_VALUE_EFFICIENCY) * PERCENTAGE) / PERCENTAGE;
    }
}
