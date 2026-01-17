package tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;
import main.AppCenter;
import milestones.Milestone;
import tickets.action.Action;
import users.Developer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Ticket {
    protected int id;
    protected TicketType type;
    protected String title;
    protected BusinessPriority businessPriority;
    protected BusinessPriority initialBusinessPriority;
    protected TicketStatus status;
    protected ExpertiseArea expertiseArea;
    protected String description;
    protected String reportedBy;
    protected String createdAt;
    protected LocalDate solvedAt;
    protected LocalDate assignedAt;
    protected String assignedTo;
    protected List<Comment> comments;
    protected List<Action> history;
    protected List<String> matchingWords;
    protected double daysToResolve;

    public Ticket() {
        comments = new ArrayList<>();
        history = new ArrayList<>();
    }

    /**
     * Updates the business priority by increasing it one level.
     */
    public final void updateBusinessPriority() {
        if (initialBusinessPriority == null) {
            initialBusinessPriority = businessPriority;
        }
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
    public final void changeStatus(LocalDate timestamp) {
        if (status == TicketStatus.OPEN) {
            status = TicketStatus.IN_PROGRESS;
        } else if (status == TicketStatus.IN_PROGRESS) {
            status = TicketStatus.RESOLVED;
            solvedAt = timestamp;
            daysToResolve = (double) ChronoUnit.DAYS.between(assignedAt, timestamp) + 1;
        } else if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.CLOSED;
            boolean ok = true;
            AppCenter appCenter = AppCenter.getInstance();
            Milestone milestone = appCenter.getMilestoneByTicketID(id);
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() != TicketStatus.CLOSED) {
                    ok = false;
                }
            }
            if (ok) {
                milestone.setStatus("COMPLETED");
                for (Milestone blockedMilestone : milestone.getBlockingFor()) {
                    blockedMilestone.setBlocked(false);
                    blockedMilestone.setStatus("ACTIVE");
                    blockedMilestone.setLastDayUpdated(timestamp);
                    for (String dev : blockedMilestone.getAssignedDevs()) {
                        Developer developer = (Developer) appCenter.getUserByUsername(dev);
                        if (blockedMilestone.getDueDate().isBefore(timestamp)) {
                            developer.addNotification("Milestone " + blockedMilestone.getName() + " was unblocked after due date. All active tickets are now CRITICAL.");
                            for (Ticket t : blockedMilestone.getTickets()) {
                                if (t.getStatus() != TicketStatus.CLOSED) {
                                    t.setBusinessPriority(BusinessPriority.CRITICAL);
                                }
                            }
                        } else {
                            developer.addNotification("Milestone " + blockedMilestone.getName() + " is now unblocked as ticket " + id + " has been CLOSED.");
                        }
                    }
                }
            }
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

    public abstract Double calculateImpactFinal();

    public abstract Double calculateRiskFinal();

    public abstract Double calculateEfficiencyFinal();
}
