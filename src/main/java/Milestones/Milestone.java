package Milestones;

import Tickets.Ticket;
import Users.Developer;
import enums.BusinessPriority;
import enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import main.AppCenter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Milestone {
    private String name;
    private LocalDate dueDate;
    private ArrayList<Milestone> blockingFor;
    private ArrayList<Ticket> tickets;
    private ArrayList<String> assignedDevs;
    private Boolean blocked;
    private LocalDate createdAt;
    private String createdBy;
    private String status;
    private ArrayList<Repartition> repartitions;
    private LocalDate lastDayUpdated;

    public Milestone() {
        blocked = true;
        blockingFor = new ArrayList<>();
        assignedDevs = new ArrayList<>();
        tickets = new ArrayList<>();
        repartitions = new ArrayList<>();
    }

    public Milestone(String name, LocalDate dueDate, ArrayList<String> blockingFor, ArrayList<Integer> tickets, ArrayList<String> assignedDevs, LocalDate createdAt, String createdBy) {
        this.name = name;
        this.dueDate = dueDate;
        this.assignedDevs = assignedDevs;
        this.createdAt = createdAt;
        lastDayUpdated = createdAt;
        this.createdBy = createdBy;

        AppCenter appCenter = AppCenter.getInstance();
        this.tickets = new ArrayList<>();
        for (Integer ticketId : tickets) {
            for (Ticket ticket : appCenter.getTickets()) {
                if (ticket.getId() == ticketId) {
                    this.tickets.add(ticket);
                }
            }
        }
        this.blockingFor = new ArrayList<>();
        for (String milestoneName : blockingFor) {
            for (Milestone milestone : appCenter.getMilestones()) {
                if (milestone.getName().equals(milestoneName)) {
                    milestone.setBlocked(true);
                    this.blockingFor.add(milestone);
                }
            }
        }
        this.repartitions = new ArrayList<>();
        for (String dev : assignedDevs) {
            repartitions.add(new Repartition((Developer) appCenter.getUserByUsername(dev)));
        }
        blocked = false;
        status = "ACTIVE";
        System.out.println("Milestone " + name + " has been created at " + createdAt + " ceva due date " + dueDate);
    }

    public void checkBusinessPriority(LocalDate nowDate) {
        if (blocked)
            return;

        boolean updated = false;
        for (int i = 3; i <= (int) ChronoUnit.DAYS.between(lastDayUpdated, nowDate) + 1; i += 3) {
            updated = true;
            for (Ticket ticket : tickets) {
                ticket.updateBusinessPriority();
            }
        }
        if (updated) {
            lastDayUpdated = nowDate;
        }
        if (((int) ChronoUnit.DAYS.between(nowDate, dueDate)) <= 1) {
            for (Ticket ticket : tickets) {
                ticket.setBusinessPriority(BusinessPriority.CRITICAL);
            }
        }
    }

    public int getDaysUntilDue(LocalDate currentDate) {
        return Math.max((int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1, 0);
    }

    public int getOverdueBy(LocalDate currentDate) {
        if (currentDate.isAfter(dueDate)) {
            return (int) ChronoUnit.DAYS.between(dueDate, currentDate) + 1;
        }
        return 0;
    }

    public double getCompletionPercentage() {
        int total = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.CLOSED) {
                total++;
            }
        }
        return (double) total / tickets.size() * 100;
    }
}
