package Users;

import enums.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Developer extends User {
    private String hireDate;
    private ExpertiseArea expertiseArea;
    private String seniority;
    private BusinessPriority businessPriority;

    public Developer() {}

    public Developer(String username, String email, String role, String hireDate, ExpertiseArea expertiseArea, String seniority) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
    }
}
