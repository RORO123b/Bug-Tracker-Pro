package commands.search;

import enums.BusinessPriority;
import fileio.FilterInput;
import tickets.Ticket;

public final class PriorityFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        BusinessPriority filterPriority = filters.getBusinessPriority();
        if (filterPriority == null) {
            return true;
        }
        return ticket.getBusinessPriority() == filterPriority;
    }
}
