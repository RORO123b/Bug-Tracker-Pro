package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;

import java.util.List;

public final class UsersFactory {

    /**
     * Creates a basic User.
     * @param username the user's name
     * @param email the user's email
     * @param role the user's role
     * @return a new User instance
     */
    public static User createUser(final String username, final String email, final String role) {
        return new User(username, email, role);
    }

    /**
     * Creates a Manager.
     * @param username the manager's name
     * @param email the manager's email
     * @param hireDate the date of hiring
     * @param subordinates list of subordinate names
     * @return a new Manager instance
     */
    public static Manager createManager(final String username,
                                        final String email,
                                        final String hireDate,
                                        final List<String> subordinates) {
        return new Manager(username, email, "MANAGER", hireDate, subordinates);
    }

    /**
     * Creates a Developer
     */
    public static Developer createDeveloper(final String username,
                                            final String email,
                                            final String hireDate,
                                            final ExpertiseArea expertiseArea,
                                            final String seniority,
                                            final BusinessPriority businessPriority) {

        Developer developer = new Developer(username, email, "DEVELOPER",
                hireDate, expertiseArea, seniority);
        developer.setBusinessPriority(businessPriority);
        return developer;
    }

    /**
     * Creates a User based on a specific role string
     */
    public static User createByRole(final String role,
                                    final String username,
                                    final String email,
                                    final String hireDate,
                                    final ExpertiseArea expertiseArea,
                                    final String seniority,
                                    final BusinessPriority businessPriority,
                                    final List<String> subordinates) {

        if ("MANAGER".equalsIgnoreCase(role)) {
            return createManager(username, email, hireDate, subordinates);
        }

        if ("DEVELOPER".equalsIgnoreCase(role)) {
            return createDeveloper(username, email, hireDate,
                    expertiseArea, seniority, businessPriority);
        }

        return createUser(username, email, role);
    }
}
