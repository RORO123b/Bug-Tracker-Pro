package users;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Manager extends User {
    private String hireDate;
    private List<String> subordinates;

    public Manager(final String username,
                   final String email,
                   final String role,
                   final String hireDate,
                   final List<String> subordinates) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }
}
