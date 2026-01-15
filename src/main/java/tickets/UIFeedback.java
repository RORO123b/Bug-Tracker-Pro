package tickets;

import enums.BusinessValue;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UIFeedback extends Ticket {
    private String uiElementId;
    private BusinessValue businessValue;
    private Integer usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;

    private static final double MAX_VALUE_RISK = 100.0;
    private static final double MAX_VALUE_IMPACT = 100.0;
    private static final double MAX_VALUE_EFFICIENCY = 20.0;
    private static final double PERCENTAGE = 100.0;

    public UIFeedback() {
        super();
        type = TicketType.UI_FEEDBACK;
        status = TicketStatus.OPEN;
    }

    @Override
    public Double calculateImpactFinal() {
        return Math.round(Math.min(PERCENTAGE, (businessValue.getValue() * usabilityScore * PERCENTAGE) / MAX_VALUE_IMPACT) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateRiskFinal() {
        return Math.round(Math.min(PERCENTAGE, ((11 - usabilityScore) * businessValue.getValue() * PERCENTAGE) / MAX_VALUE_RISK) * PERCENTAGE) / PERCENTAGE;
    }

    @Override
    public Double calculateEfficiencyFinal() {
        return Math.round(Math.min(PERCENTAGE, ((usabilityScore + businessValue.getValue()) / daysToResolve * PERCENTAGE) / MAX_VALUE_EFFICIENCY) * PERCENTAGE) / PERCENTAGE;
    }
}
