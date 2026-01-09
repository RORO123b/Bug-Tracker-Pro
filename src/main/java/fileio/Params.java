package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import enums.Frequency;
import enums.Severity;
import enums.BusinessValue;
import enums.CustomerDemand;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Params {

    private int id;
    private TicketType type;
    private String title;
    private BusinessPriority businessPriority;
    private TicketStatus status;
    private ExpertiseArea expertiseArea;
    private String description;
    private String reportedBy;

    private String expectedBehavior;
    private String actualBehavior;
    private Frequency frequency;
    private Severity severity;
    private String environment;
    private Integer errorCode;

    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    private String uiElementId;
    private Integer usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;
}
