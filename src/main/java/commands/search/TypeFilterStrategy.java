package commands.search;

import enums.TicketType;
import fileio.FilterInput;
import tickets.Ticket;

public class TypeFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(Ticket ticket, FilterInput filters) {
        String filterType = filters.getType();
        if (filterType == null || filterType.isEmpty()) {
            return true;
        }
        return ticket.getType() == TicketType.valueOf(filterType);
    }
}
