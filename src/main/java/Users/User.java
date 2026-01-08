package Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected String username;
    protected String email;
    protected String role;

    public User() {}

    public User(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public User(String username) {
        this.username = username;
    }
}
