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
     * @param timestamp the current date for status change
     */
    public final void changeStatus(final LocalDate timestamp) {
        if (status == TicketStatus.OPEN) {
            status = TicketStatus.IN_PROGRESS;
        } else if (status == TicketStatus.IN_PROGRESS) {
            status = TicketStatus.RESOLVED;
            solvedAt = timestamp;
            daysToResolve = (double) ChronoUnit.DAYS.between(assignedAt, timestamp) + 1;
        } else if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.CLOSED;
            checkAndCompleteMilestone(timestamp);
        }
    }

    private void checkAndCompleteMilestone(final LocalDate timestamp) {
        AppCenter appCenter = AppCenter.getInstance();
        Milestone milestone = appCenter.getMilestoneByTicketID(id);
        if (!allTicketsClosed(milestone)) {
            return;
        }
        milestone.setStatus("COMPLETED");
        unblockDependentMilestones(milestone, timestamp, appCenter);
    }

    private boolean allTicketsClosed(final Milestone milestone) {
        for (Ticket ticket : milestone.getTickets()) {
            if (ticket.getStatus() != TicketStatus.CLOSED) {
                return false;
            }
        }
        return true;
    }

    private void unblockDependentMilestones(final Milestone milestone, 
                                            final LocalDate timestamp,
                                            final AppCenter appCenter) {
        for (Milestone blockedMilestone : milestone.getBlockingFor()) {
            blockedMilestone.setBlocked(false);
            blockedMilestone.setStatus("ACTIVE");
            blockedMilestone.setLastDayUpdated(timestamp);
                notifyDevelopersAboutUnblock(blockedMilestone, timestamp, appCenter);
        }
    }

    private void notifyDevelopersAboutUnblock(final Milestone blockedMilestone,
                                              final LocalDate timestamp,
                                              final AppCenter appCenter) {
        boolean isOverdue = blockedMilestone.getDueDate().isBefore(timestamp);
        for (String dev : blockedMilestone.getAssignedDevs()) {
            Developer developer = (Developer) appCenter.getUserByUsername(dev);
                if (isOverdue) {
                notifyOverdueMilestone(developer, blockedMilestone);
                setTicketsToCritical(blockedMilestone);
            } else {
                notifyUnblockedMilestone(developer, blockedMilestone);
            }
        }
    }

    private void notifyOverdueMilestone(final Developer developer, 
                                        final Milestone blockedMilestone) {
        developer.addNotification("Milestone " + blockedMilestone.getName()
                + " was unblocked after due date. "
                + "All active tickets are now CRITICAL.");
    }

    private void notifyUnblockedMilestone(final Developer developer,
                                          final Milestone blockedMilestone) {
        developer.addNotification("Milestone " + blockedMilestone.getName()
                + " is now unblocked as ticket " + id + " has been CLOSED.");
    }

    private void setTicketsToCritical(final Milestone blockedMilestone) {
        for (Ticket ticket : blockedMilestone.getTickets()) {
            if (ticket.getStatus() != TicketStatus.CLOSED) {
                ticket.setBusinessPriority(BusinessPriority.CRITICAL);
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

    /**
     * Calculates the final impact score for this ticket
     * @return the calculated impact score
     */
    public abstract Double calculateImpactFinal();

    /**
     * Calculates the final risk score for this ticket
     * @return the calculated risk score
     */
    public abstract Double calculateRiskFinal();

    /**
     * Calculates the final efficiency score for this ticket
     * @return the calculated efficiency score
     */
    public abstract Double calculateEfficiencyFinal();
}
