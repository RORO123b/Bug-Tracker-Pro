package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import lombok.Getter;
import lombok.Setter;
import tickets.Ticket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public final class Developer extends User {
    private String hireDate;
    private ExpertiseArea expertiseArea;
    private String seniority;
    private BusinessPriority businessPriority;
    private List<Ticket> assignedTickets;
    private double performanceScore;
    private List<String> notifications;

    public Developer() {
        assignedTickets = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public Developer(final String username,
                     final String email,
                     final String role,
                     final String hireDate,
                     final ExpertiseArea expertiseArea,
                     final String seniority) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
        assignedTickets = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Adds a ticket to the assigned list and sorts it
     * @param ticket the ticket to add
     */
    public void addTicket(final Ticket ticket) {
        assignedTickets.add(ticket);

        assignedTickets.sort(
                Comparator.comparing(Ticket::getBusinessPriority, Comparator.reverseOrder())
                        .thenComparing(Ticket::getCreatedAt)
                        .thenComparing(Ticket::getId)
        );
    }

    /**
     * Removes a ticket from the assigned list.
     * @param ticket the ticket to remove
     */
    public void removeTicket(final Ticket ticket) {
        assignedTickets.remove(ticket);
    }

    /**
     * Checks if the developer can handle a specific expertise area
     * @param ticketExpertise the expertise area of the ticket
     * @return true if handleable, false otherwise
     */
    public boolean canHandleExpertise(final ExpertiseArea ticketExpertise) {
        if (this.expertiseArea == ExpertiseArea.FULLSTACK) {
            return true;
        }
        if (this.expertiseArea == ticketExpertise) {
            return true;
        }
        if (this.expertiseArea == ExpertiseArea.FRONTEND
                && ticketExpertise == ExpertiseArea.DESIGN) {
            return true;
        }
        if (this.expertiseArea == ExpertiseArea.DESIGN
                && ticketExpertise == ExpertiseArea.FRONTEND) {
            return true;
        }
        if (this.expertiseArea == ExpertiseArea.BACKEND
                && ticketExpertise == ExpertiseArea.DB) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the developer can handle a specific business priority based on seniority
     * @param ticketPriority the priority level
     * @return true if handleable, false otherwise
     */
    public boolean canHandlePriority(final BusinessPriority ticketPriority) {
        if ("SENIOR".equals(this.seniority)) {
            return true;
        }
        if ("MID".equals(this.seniority)) {
            return ticketPriority != BusinessPriority.CRITICAL;
        }
        if ("JUNIOR".equals(this.seniority)) {
            return ticketPriority == BusinessPriority.LOW
                    || ticketPriority == BusinessPriority.MEDIUM;
        }
        return false;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }
}
