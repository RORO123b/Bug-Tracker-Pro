package commands.search;

import fileio.FilterInput;
import main.AppCenter;
import users.Developer;
import users.Manager;
import users.User;

import java.util.ArrayList;
import java.util.List;

public class DeveloperSearchTypeStrategy implements SearchTypeStrategy {
    @Override
    public List<Developer> execute(User requester, FilterInput filters) {
        if (!"MANAGER".equals(requester.getRole())) {
            return new ArrayList<>();
        }

        Manager manager = (Manager) requester;
        AppCenter appCenter = AppCenter.getInstance();
        List<Developer> initialPool = new ArrayList<>();

        for (String subordinate : manager.getSubordinates()) {
            User user = appCenter.getUserByUsername(subordinate);
            initialPool.add((Developer) user);
        }

        return new DeveloperSearchEngine().search(initialPool, filters);
    }
}
