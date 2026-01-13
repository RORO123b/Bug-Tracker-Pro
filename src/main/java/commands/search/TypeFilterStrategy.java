package commands.search;

import enums.TicketType;
import fileio.FilterInput;
import tickets.Ticket;

public final class TypeFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        String filterType = filters.getType();
        if (filterType == null || filterType.isEmpty()) {
            return true;
        }
        return ticket.getType() == TicketType.valueOf(filterType);
    }
}
