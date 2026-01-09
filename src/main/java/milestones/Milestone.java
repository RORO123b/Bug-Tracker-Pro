package milestones;

import tickets.Ticket;
import users.Developer;
import enums.BusinessPriority;
import enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import main.AppCenter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Getter
@Setter
public final class Milestone {

    private static final int BUSINESS_PRIORITY_UPDATE_INTERVAL = 3;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

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

    public Milestone(final String name,
                     final LocalDate dueDate,
                     final ArrayList<String> blockingFor,
                     final ArrayList<Integer> tickets,
                     final ArrayList<String> assignedDevs,
                     final LocalDate createdAt,
                     final String createdBy) {
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
    }

    /**
     * @param nowDate the current date to check against
     */
    public void checkBusinessPriority(final LocalDate nowDate) {
        if (blocked) {
            return;
        }

        boolean updated = false;
        long daysDiff = ChronoUnit.DAYS.between(lastDayUpdated, nowDate) + 1;
        for (int i = BUSINESS_PRIORITY_UPDATE_INTERVAL; i <= (int) daysDiff;
             i += BUSINESS_PRIORITY_UPDATE_INTERVAL) {
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

    /**
     * @param currentDate the current date
     * @return number of days until due
     */
    public int getDaysUntilDue(final LocalDate currentDate) {
        return Math.max((int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1, 0);
    }

    /**
     * @param currentDate the current date
     * @return number of days overdue
     */
    public int getOverdueBy(final LocalDate currentDate) {
        if (currentDate.isAfter(dueDate)) {
            return (int) ChronoUnit.DAYS.between(dueDate, currentDate) + 1;
        }
        return 0;
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
        return (double) total / tickets.size() * PERCENTAGE_MULTIPLIER;
    }
}
