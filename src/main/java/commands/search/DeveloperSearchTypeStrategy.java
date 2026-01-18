package commands.search;

import fileio.FilterInput;
import main.AppCenter;
import users.Developer;
import users.Manager;
import users.User;

import java.util.ArrayList;
import java.util.List;

public final class DeveloperSearchTypeStrategy implements SearchTypeStrategy {
    /**
     * Executes the developer search strategy for a given manager
     * @param requester the user requesting the search
     * @param filters the filter criteria
     * @return the list of developers found
     */
    @Override
    public List<Developer> execute(final User requester, final FilterInput filters) {
        if (!requester.getRole().equals("MANAGER")) {
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
