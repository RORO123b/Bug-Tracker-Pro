package fileio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserInput {
    private String username;
    private String email;
    private String role;

    private String hireDate;
    private List<String> subordinates;

    private ExpertiseArea expertiseArea;
    private String seniority;
    private BusinessPriority businessPriority;
}
