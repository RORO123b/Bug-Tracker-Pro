package commands.search;

import enums.BusinessPriority;
import fileio.FilterInput;
import tickets.Ticket;

public class PriorityFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(Ticket ticket, FilterInput filters) {
        BusinessPriority filterPriority = filters.getBusinessPriority();
        if (filterPriority == null) {
            return true;
        }
        return ticket.getBusinessPriority() == filterPriority;
    }
}
