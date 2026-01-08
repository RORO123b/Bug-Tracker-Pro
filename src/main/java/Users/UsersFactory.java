package Users;

import enums.BusinessPriority;
import enums.ExpertiseArea;

import java.util.List;

public class UsersFactory {

    public static User createUser(String username, String email, String role) {
        return new User(username, email, role);
    }

    public static Manager createManager(String username, String email, String hireDate, List<String> subordinates) {
        return new Manager(username, email, "MANAGER", hireDate, subordinates);
    }

    public static Developer createDeveloper(String username,
                                            String email,
                                            String hireDate,
                                            ExpertiseArea expertiseArea,
                                            String seniority,
                                            BusinessPriority businessPriority) {

        Developer developer = new Developer(username, email, "DEVELOPER", hireDate, expertiseArea, seniority);
        developer.setBusinessPriority(businessPriority);
        return developer;
    }

    public static User createByRole(String role,
                                    String username,
                                    String email,
                                    String hireDate,
                                    ExpertiseArea expertiseArea,
                                    String seniority,
                                    BusinessPriority businessPriority,
                                    List<String> subordinates) {

        if ("MANAGER".equalsIgnoreCase(role)) {
            return createManager(username, email, hireDate, subordinates);
        }

        if ("DEVELOPER".equalsIgnoreCase(role)) {
            return createDeveloper(username, email, hireDate, expertiseArea, seniority, businessPriority);
        }

        // Reporter
        return createUser(username, email, role);
    }
}
