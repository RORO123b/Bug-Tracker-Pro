package commands.search;

import fileio.FilterInput;
import tickets.Ticket;

import java.time.LocalDate;

public final class CreatedBeforeFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        if (filters.getCreatedBefore() == null) {
            return true;
        }
        LocalDate createdAt = LocalDate.parse(ticket.getCreatedAt());
        return createdAt.isBefore(filters.getCreatedBefore())
                || createdAt.isEqual(filters.getCreatedBefore());
    }
}
