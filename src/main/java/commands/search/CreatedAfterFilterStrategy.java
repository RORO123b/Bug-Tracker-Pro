package commands.search;

import fileio.FilterInput;
import tickets.Ticket;

import java.time.LocalDate;

public final class CreatedAfterFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        LocalDate createdAfter = filters.getCreatedAfter();
        if (createdAfter == null) {
            return true;
        }
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isBefore(createdAfter);
    }
}
