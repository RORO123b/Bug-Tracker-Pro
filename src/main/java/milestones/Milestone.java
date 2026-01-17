package milestones;

import tickets.Ticket;
import tickets.action.ActionBuilder;
import enums.BusinessPriority;
import enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import main.AppCenter;
import users.Developer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Getter
@Setter
public final class Milestone {

    private static final int BUSINESS_PRIORITY_UPDATE_INTERVAL = 3;
    private static final double PERCENTAGE = 100.0;

    private String name;
    private LocalDate dueDate;
    private ArrayList<Milestone> blockingFor;
    private ArrayList<Ticket> tickets;
    private ArrayList<String> assignedDevs;
    private Boolean blocked;
    private LocalDate createdAt;
    private String createdBy;
    private String status;
    private LocalDate lastDayUpdated;
    private int overdueBy;
    private int daysUntilDue;
    private boolean dueTomorrowNotified;

    public Milestone() {
        blocked = true;
        blockingFor = new ArrayList<>();
        assignedDevs = new ArrayList<>();
        tickets = new ArrayList<>();
        dueTomorrowNotified = false;
    }

    public Milestone(final String name,
                     final LocalDate dueDate,
                     final ArrayList<String> blockingFor,
                     final ArrayList<Integer> tickets,
                     final ArrayList<String> assignedDevs,
                     final LocalDate createdAt,
                     final String createdBy) {
        this.name = name;
        this.dueDate = dueDate;
        this.assignedDevs = new ArrayList<>(assignedDevs);

        AppCenter appCenter = AppCenter.getInstance();
        for (String dev : assignedDevs) {
            Developer developer = (Developer) appCenter.getUserByUsername(dev);
            developer.addNotification("New milestone " + name + " has been created with due date " + dueDate + ".");
        }

        this.createdAt = createdAt;
        lastDayUpdated = createdAt;
        this.createdBy = createdBy;

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
        blocked = false;
        status = "ACTIVE";
    }

    /**
     * @param nowDate the current date to check against
     */
    public void checkBusinessPriority(final LocalDate nowDate) {
        if (blocked) {
            return;
        }
        AppCenter appCenter = AppCenter.getInstance();
        int totalDays = (int) ChronoUnit.DAYS.between(createdAt, nowDate);
        int lastUpdateDays = (int) ChronoUnit.DAYS.between(createdAt, lastDayUpdated);

        int currentInterval = (int) (totalDays / BUSINESS_PRIORITY_UPDATE_INTERVAL);
        int lastInterval = (int) (lastUpdateDays / BUSINESS_PRIORITY_UPDATE_INTERVAL);

        for (int i = lastInterval + 1; i <= currentInterval; i++) {
            for (Ticket ticket : tickets) {
                if (ticket.getStatus() != TicketStatus.CLOSED) {
                    ticket.updateBusinessPriority();
                    checkAndRemoveFromDev(ticket, appCenter, nowDate);
                }
            }
        }
        if (currentInterval > lastInterval) {
            lastDayUpdated = nowDate;
        }
        if (((int) ChronoUnit.DAYS.between(nowDate, dueDate)) == 1 && !dueTomorrowNotified) {
            dueTomorrowNotified = true;
            for (String dev : assignedDevs) {
                Developer developer = (Developer) appCenter.getUserByUsername(dev);
                developer.addNotification("Milestone " + name
                        + " is due tomorrow. All unresolved tickets are now CRITICAL.");
            }
            for (Ticket ticket : tickets) {
                if (ticket.getStatus() != TicketStatus.CLOSED) {
                    ticket.setBusinessPriority(BusinessPriority.CRITICAL);
                    checkAndRemoveFromDev(ticket, appCenter, nowDate);
                }
            }
        }
    }

    private void checkAndRemoveFromDev(final Ticket ticket, final AppCenter appCenter,
                                       final LocalDate nowDate) {
        if (ticket.getAssignedTo() == null || ticket.getAssignedTo().isEmpty()) {
            return;
        }
        Developer dev = (Developer) appCenter.getUserByUsername(ticket.getAssignedTo());
        if (!appCenter.requiredSeniority(ticket).contains(dev.getSeniority())) {
            dev.removeTicket(ticket);
            ticket.setStatus(TicketStatus.OPEN);
            ticket.setAssignedTo("");
            ticket.setAssignedAt(null);
            ticket.getHistory().add(new ActionBuilder()
                    .action("REMOVED_FROM_DEV")
                    .removedDev(dev.getUsername())
                    .by("system")
                    .timestamp(nowDate)
                    .build());
        }
    }

    /**
     * @param currentDate the current date
     */
    public void calculateDaysUntilDue(final LocalDate currentDate) {
        if (status.equals("COMPLETED")) {
            return;
        }
        daysUntilDue = Math.max((int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1, 0);
    }

    /**
     * @param currentDate the current date
     */
    public void calculateOverdueBy(final LocalDate currentDate) {
        if (status.equals("COMPLETED")) {
            return;
        }
        if (currentDate.isAfter(dueDate)) {
            overdueBy = (int) ChronoUnit.DAYS.between(dueDate, currentDate) + 1;
        } else {
            overdueBy = 0;
        }
    }

    /**
     * @return the percentage of closed tickets
     */
    public double getCompletionPercentage() {
        int total = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.CLOSED) {
                total++;
            }
        }
        if (tickets.isEmpty()) {
            return 0.0;
        }
        double percentage = (double) total / tickets.size();
        return Math.round(percentage * PERCENTAGE) / PERCENTAGE;
    }
}
