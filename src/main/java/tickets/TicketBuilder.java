package tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;

public abstract class TicketBuilder<T extends Ticket, B extends TicketBuilder<T, B>> {

    protected T ticket;

    /**
     * Returns the current builder instance.
     * @return the builder instance
     */
    protected abstract B self();

    /**
     * @param id the ticket identifier
     * @return the builder instance
     */
    public final B id(final int id) {
        ticket.id = id;
        return self();
    }

    /**
     * @param title the ticket title
     * @return the builder instance
     */
    public final B title(final String title) {
        ticket.title = title;
        return self();
    }

    /**
     * @param type the ticket type
     * @return the builder instance
     */
    public final B type(final TicketType type) {
        ticket.type = type;
        return self();
    }

    /**
     * @param priority the business priority
     * @return the builder instance
     */
    public final B businessPriority(final BusinessPriority priority) {
        if (ticket.reportedBy.equals("")) {
            ticket.businessPriority = BusinessPriority.LOW;
            return self();
        }
        ticket.businessPriority = priority;
        return self();
    }

    /**
     * @param status the ticket status
     * @return the builder instance
     */
    public final B status(final TicketStatus status) {
        ticket.status = status;
        return self();
    }

    /**
     * @param area the required expertise area
     * @return the builder instance
     */
    public final B expertiseArea(final ExpertiseArea area) {
        ticket.expertiseArea = area;
        return self();
    }

    /**
     * @param description the ticket description
     * @return the builder instance
     */
    public final B description(final String description) {
        ticket.description = description;
        return self();
    }

    /**
     * @param reportedBy the username of the reporter
     * @return the builder instance
     */
    public final B reportedBy(final String reportedBy) {
        ticket.reportedBy = reportedBy;
        return self();
    }

    /**
     * @param createdAt the creation timestamp
     * @return the builder instance
     */
    public final B createdAt(final String createdAt) {
        ticket.setCreatedAt(createdAt);
        return self();
    }

    /**
     * Builds the final ticket instance.
     * @return the constructed ticket
     */
    public abstract T build();
}
