package commands.search;

import enums.TicketStatus;
import fileio.FilterInput;
import tickets.Ticket;
import users.Developer;

public class AvailabilityFilterStrategy implements FilterStrategy<Ticket> {
    private final Developer currentDev;

    public AvailabilityFilterStrategy(Developer currentDev) {
        this.currentDev = currentDev;
    }

    @Override
    public boolean matches(Ticket ticket, FilterInput filters) {
        if (!filters.isAvailableForAssignment()) {
            return true;
        }

        if (ticket.getStatus() != TicketStatus.OPEN)
            return false;

        if (!currentDev.canHandleExpertise(ticket.getExpertiseArea()))
            return false;

        if (!currentDev.canHandlePriority(ticket.getBusinessPriority()))
            return false;

        return true;
    }
}
