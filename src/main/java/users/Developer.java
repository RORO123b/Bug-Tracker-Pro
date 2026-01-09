package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Developer extends User {
    private String hireDate;
    private ExpertiseArea expertiseArea;
    private String seniority;
    private BusinessPriority businessPriority;

    public Developer() { }

    public Developer(final String username,
                     final String email,
                     final String role,
                     final String hireDate,
                     final ExpertiseArea expertiseArea,
                     final String seniority) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
    }
}
