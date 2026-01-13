package commands.search;

import enums.TicketStatus;
import fileio.FilterInput;
import tickets.Ticket;
import users.Developer;

public final class AvailabilityFilterStrategy implements FilterStrategy<Ticket> {
    private final Developer currentDev;

    public AvailabilityFilterStrategy(final Developer currentDev) {
        this.currentDev = currentDev;
    }

    /**
     * Checks if a ticket matches the availability criteria for the current developer
     * @param ticket the ticket to check
     * @param filters the filter criteria
     * @return true if it matches, false otherwise
     */
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        if (!filters.isAvailableForAssignment()) {
            return true;
        }

        if (ticket.getStatus() != TicketStatus.OPEN) {
            return false;
        }

        if (!currentDev.canHandleExpertise(ticket.getExpertiseArea())) {
            return false;
        }

        if (!currentDev.canHandlePriority(ticket.getBusinessPriority())) {
            return false;
        }

        return true;
    }
}
