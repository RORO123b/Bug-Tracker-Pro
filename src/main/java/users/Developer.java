package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;
import tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.sqrt;

@Getter
@Setter
public final class Developer extends User {
    private static final int JUNIOR_BONUS = 5;
    private static final int MID_BONUS = 15;
    private static final int SENIOR_BONUS = 30;
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

    public List<Ticket> getClosedTicketsLastMonth(LocalDate lastMonth) {
        List<Ticket> closedTickets = new ArrayList<>();
        for (Ticket ticket : assignedTickets) {
            if (ticket.getStatus() == TicketStatus.CLOSED && lastMonth.getMonth().equals(ticket.getSolvedAt().getMonth())) {
                closedTickets.add(ticket);
            }
        }
        return closedTickets;
    }

    public double getAverageResolutionTime(LocalDate lastMonth) {
        List<Ticket> closedTickets = getClosedTicketsLastMonth(lastMonth);
        if (closedTickets.isEmpty()) {
            return 0.0;
        }
        double averageResolutionTime = 0.0;
        for (Ticket ticket : closedTickets) {
            averageResolutionTime += (int) ChronoUnit.DAYS.between(ticket.getAssignedAt(), ticket.getSolvedAt()) + 1;
        }
        averageResolutionTime /= closedTickets.size();
        return averageResolutionTime;
    }

    public double getPerformanceScore(LocalDate timestamp) {
        List<Ticket> closedTickets = getClosedTicketsLastMonth(timestamp.minusMonths(1));
        if (closedTickets.isEmpty()) {
            return 0.0;
        }
        int bugTickets = 0;
        int featureTickets = 0;
        int uiTickets = 0;
        for (Ticket ticket : closedTickets) {
           if (ticket.getType() == TicketType.BUG) {
               bugTickets++;
           } else if (ticket.getType() == TicketType.FEATURE_REQUEST) {
               featureTickets++;
           } else if (ticket.getType() == TicketType.UI_FEEDBACK) {
               uiTickets++;
           }
        }
        if (seniority.equals("JUNIOR")) {
            double averageResolvedTicketType = (bugTickets + featureTickets + uiTickets) / 3.0;
            double standardDeviation = sqrt((Math.pow((bugTickets - averageResolvedTicketType), 2) + Math.pow((featureTickets - averageResolvedTicketType), 2) + Math.pow((uiTickets - averageResolvedTicketType), 2)) / 3);
            double ticketDiversityFactor = standardDeviation / averageResolvedTicketType;
            performanceScore = Math.max(0, 0.5 * closedTickets.size() - ticketDiversityFactor) + JUNIOR_BONUS;
        } else if (seniority.equals("MID")) {
            int highPriorityTickets = 0;
            double averageResolutionTime = getAverageResolutionTime(timestamp.minusMonths(1));
            for (Ticket ticket : closedTickets) {
                if (ticket.getBusinessPriority() == BusinessPriority.HIGH || ticket.getBusinessPriority() == BusinessPriority.CRITICAL) {
                    highPriorityTickets++;
                }
            }
            performanceScore = Math.max(0, 0.5 * closedTickets.size() + 0.7 * highPriorityTickets - 0.3 * averageResolutionTime) + MID_BONUS;
        } else if (seniority.equals("SENIOR")) {
            int highPriorityTickets = 0;
            double averageResolutionTime = getAverageResolutionTime(timestamp.minusMonths(1));
            for (Ticket ticket : closedTickets) {
                if (ticket.getBusinessPriority() == BusinessPriority.HIGH || ticket.getBusinessPriority() == BusinessPriority.CRITICAL) {
                    highPriorityTickets++;
                }
            }
            performanceScore = Math.max(0, 0.5 * closedTickets.size() + 1.0 * highPriorityTickets - 0.5 * averageResolutionTime) + SENIOR_BONUS;
        }
        return performanceScore;
    }
}
