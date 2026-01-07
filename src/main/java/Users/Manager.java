package Users;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Manager extends User{
    private String hireDate;
    private List<String> subordinates;

    public Manager() {}
    public Manager(String username, String email, String role, String hireDate, List<String> subordinates) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }
}
