package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import enums.BusinessPriority;
import enums.ExpertiseArea;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FilterInput {
    private String searchType;
    private BusinessPriority businessPriority;
    private String type;
    private LocalDate createdAfter;
    private LocalDate createdBefore;
    private boolean availableForAssignment;
    private ExpertiseArea expertiseArea;
    private String seniority;
    private Double performanceScoreAbove;
    private Double performanceScoreBelow;
    private ArrayList<String> keywords;
}
