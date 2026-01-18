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

    private static final double MAX_VALUE_IMPACT = 48.0;
    private static final double MAX_VALUE_RISK = 12.0;
    private static final double MAX_VALUE_EFFICIENCY = 70.0;
    private static final double PERCENTAGE = 100.0;
    private static final double EFFICIENCY_MULTIPLIER = 10.0;

    Bug() {
        super();
        type = TicketType.BUG;
        status = TicketStatus.OPEN;
    }

    @Override
    public final Double calculateImpactFinal() {
        return Math.round(Math.min(PERCENTAGE,
                (frequency.getValue() * businessPriority.getValue()
                        * severity.getValue() * PERCENTAGE)
                / MAX_VALUE_IMPACT) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public final Double calculateRiskFinal() {
        return Math.round(Math.min(PERCENTAGE,
                (frequency.getValue() * severity.getValue()
                        * PERCENTAGE) / MAX_VALUE_RISK)
                * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public final Double calculateEfficiencyFinal() {
        return Math.round(Math.min(PERCENTAGE,
                (frequency.getValue() + severity.getValue())
                        * EFFICIENCY_MULTIPLIER / daysToResolve * PERCENTAGE
                / MAX_VALUE_EFFICIENCY) * PERCENTAGE) / PERCENTAGE;
    }
}
