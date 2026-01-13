package commands.search;

import fileio.FilterInput;
import tickets.Ticket;
import users.Developer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class TicketSearchEngine {
    private final List<FilterStrategy<Ticket>> strategies;

    public TicketSearchEngine(final Developer currentDev) {
        strategies = new ArrayList<>();
        strategies.add(new PriorityFilterStrategy());
        strategies.add(new TypeFilterStrategy());
        strategies.add(new CreatedAfterFilterStrategy());
        strategies.add(new AvailabilityFilterStrategy(currentDev));
        strategies.add(new KeywordFilterStrategy());
    }

    /**
     * Searches for tickets based on the initial pool and filters
     * @param initialPool the list of tickets to search through
     * @param filters the criteria for filtering
     * @return a sorted list of matching tickets
     */
    public List<Ticket> search(final List<Ticket> initialPool, final FilterInput filters) {
        return initialPool.stream()
                .filter(ticket -> strategies.stream().allMatch(s -> s.matches(ticket, filters)))
                .sorted(Comparator.comparing(Ticket::getCreatedAt).thenComparing(Ticket::getId))
                .collect(Collectors.toList());
    }
}
