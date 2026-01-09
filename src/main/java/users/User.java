package users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected String username;
    protected String email;
    protected String role;

    public User() { }

    public User(final String username, final String email, final String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public User(final String username) {
        this.username = username;
    }
}
