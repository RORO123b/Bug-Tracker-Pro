package commands.search;

import fileio.FilterInput;
import tickets.Ticket;

import java.time.LocalDate;

public class CreatedAfterFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(Ticket ticket, FilterInput filters) {
        LocalDate createdAfter = filters.getCreatedAfter();
        if (createdAfter == null) {
            return true;
        }
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isBefore(createdAfter);
    }
}
