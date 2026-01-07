package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Params {
    public enum TicketType {
        BUG,
        FEATURE_REQUEST,
        UI_FEEDBACK
    }

    @Getter
    public enum BusinessPriority {
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        CRITICAL(4);

        private final int value;

        BusinessPriority(int value) {
            this.value = value;
        }

    }

    public enum TicketStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }

    public enum ExpertiseArea {
        FRONTEND,
        BACKEND,
        DEVOPS,
        DESIGN,
        DB
    }

    @Getter
    public enum Frequency {
        RARE(1),
        OCCASIONAL(2),
        FREQUENT(3),
        ALWAYS(4);

        private final int value;

        Frequency(int value) {
            this.value = value;
        }

    }

    @Getter
    public enum Severity {
        MINOR(1),
        MODERATE(2),
        SEVERE(3);

        private final int value;

        Severity(int value) {
            this.value = value;
        }

    }

    @Getter
    public enum BusinessValue {
        S(1),
        M(3),
        L(6),
        XL(10);

        private final int value;

        BusinessValue(int value) {
            this.value = value;
        }

    }

    @Getter
    public enum CustomerDemand {
        LOW(1),
        MEDIUM(3),
        HIGH(6),
        VERY_HIGH(10);

        private final int value;

        CustomerDemand(int value) {
            this.value = value;
        }

    }

    private int id;
    private UserInput.TicketType type;
    private String title;
    private UserInput.BusinessPriority businessPriority;
    private UserInput.TicketStatus status;
    private UserInput.ExpertiseArea expertiseArea;
    private String description;
    private String reportedBy;

    private String expectedBehavior;
    private String actualBehavior;
    private UserInput.Frequency frequency;
    private UserInput.Severity severity;
    private String environment;
    private Integer errorCode;

    private UserInput.BusinessValue businessValue;
    private UserInput.CustomerDemand customerDemand;

    private String uiElementId;
    private Integer usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;
}
