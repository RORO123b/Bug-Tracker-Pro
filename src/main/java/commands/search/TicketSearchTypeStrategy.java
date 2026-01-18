package commands.search;

import fileio.FilterInput;
import main.AppCenter;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.List;

public final class TicketSearchTypeStrategy implements SearchTypeStrategy {
    @Override
    public List<Ticket> execute(final User requester, final FilterInput filters) {
        AppCenter appCenter = AppCenter.getInstance();
        List<Ticket> initialPool = new ArrayList<>();

        if (requester.getRole().equals("MANAGER")) {
            initialPool.addAll(appCenter.getTickets());
        } else if (requester.getRole().equals("DEVELOPER")) {
            Developer dev = (Developer) requester;
            initialPool.addAll(appCenter.getOpenTicketsForDeveloper(dev));
        }

        Developer currentDev = null;
        if (requester.getRole().equals("DEVELOPER")) {
            currentDev = (Developer) requester;
        }
        return new TicketSearchEngine(currentDev).search(initialPool, filters);
    }
}
