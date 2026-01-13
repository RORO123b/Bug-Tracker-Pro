package tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;
import tickets.action.Action;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Ticket {
    protected int id;
    protected TicketType type;
    protected String title;
    protected BusinessPriority businessPriority;
    protected TicketStatus status;
    protected ExpertiseArea expertiseArea;
    protected String description;
    protected String reportedBy;
    protected String createdAt;
    protected String solvedAt;
    protected String assignedAt;
    protected String assignedTo;
    protected List<Comment> comments;
    protected List<Action> history;
    protected List<String> matchingWords;

    public Ticket() {
        comments = new ArrayList<>();
        history = new ArrayList<>();
    }

    /**
     * Updates the business priority by increasing it one level.
     */
    public final void updateBusinessPriority() {
        if (businessPriority == BusinessPriority.LOW) {
            businessPriority = BusinessPriority.MEDIUM;
        } else if (businessPriority == BusinessPriority.MEDIUM) {
            businessPriority = BusinessPriority.HIGH;
        } else if (businessPriority == BusinessPriority.HIGH) {
            businessPriority = BusinessPriority.CRITICAL;
        }
    }

    /**
     * Changes the ticket status to the next status in the workflow
     */
    public final void changeStatus() {
        if (status == TicketStatus.OPEN) {
            status = TicketStatus.IN_PROGRESS;
        } else if (status == TicketStatus.IN_PROGRESS) {
            status = TicketStatus.RESOLVED;
        } else if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.CLOSED;
        }
    }

    /**
     * Undoes the last status change.
     */
    public final void undoChangeStatus() {
        if (status == TicketStatus.CLOSED) {
            status = TicketStatus.RESOLVED;
        } else if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.IN_PROGRESS;
        }
    }

    /**
     * Checks if a developer was previously assigned to this ticket.
     * @param username the username to check
     * @return true if the developer was previously assigned, false otherwise
     */
    public final boolean checkPastDevs(final String username) {
        for (Action action : history) {
            if (action.getAction().equals("ASSIGNED") && action.getBy().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
