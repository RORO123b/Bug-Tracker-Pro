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
public class Developer extends User {
    private String hireDate;
    private ExpertiseArea expertiseArea;
    private String seniority;
    private BusinessPriority businessPriority;
    private List<Ticket> assignedTickets;
    private double performanceScore;

    public Developer() {
        assignedTickets = new ArrayList<>();
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
    }

    public void addTicket(final Ticket ticket) {
        assignedTickets.add(ticket);

        assignedTickets.sort(
                Comparator.comparing(Ticket::getBusinessPriority, Comparator.reverseOrder())
                        .thenComparing(Ticket::getCreatedAt)
                        .thenComparing(Ticket::getId)
        );
    }

    public void removeTicket(final Ticket ticket) {
        if (assignedTickets.contains(ticket)) {
            assignedTickets.remove(ticket);
        }
    }

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
}
