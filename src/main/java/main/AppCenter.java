package main;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import milestones.Milestone;
import tickets.Ticket;
import users.User;
import enums.Phases;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public final class AppCenter {
    private static final int TESTING_PHASE_DURATION = 12;
    private static AppCenter instance;

    private Phases currentPeriod;
    private LocalDate datePeriodStart;
    private List<Ticket> tickets;
    private List<User> users;
    private List<Milestone> milestones;

    private AppCenter() {
        currentPeriod = Phases.TESTING;
        tickets = new ArrayList<>();
        users = new ArrayList<>();
        milestones = new ArrayList<>();
    }

    private void checkTransition(final LocalDate currentDate) {
        if (currentPeriod == Phases.TESTING) {
            if ((int) ChronoUnit.DAYS.between(datePeriodStart, currentDate)
                    + 1 >= TESTING_PHASE_DURATION) {
                currentPeriod = Phases.DEVELOPMENT;
                datePeriodStart = currentDate;
            }
        }
    }

    private void updateMilestones(final LocalDate currentDate) {
        for (Milestone milestone : milestones) {
            milestone.checkBusinessPriority(currentDate);
            boolean ok = true;
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() != TicketStatus.CLOSED) {
                    ok = false;
                }
            }
            if (ok) {
                milestone.setStatus("COMPLETED");
            }
            milestone.calculateOverdueBy(currentDate);
            milestone.calculateDaysUntilDue(currentDate);
        }
    }

    /**
     * @param currentDate the current date to process updates for
     */
    public void updates(final LocalDate currentDate) {
        checkTransition(currentDate);
        updateMilestones(currentDate);
    }

    /**
     * Resets the singleton instance.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * @return the singleton instance of AppCenter
     */
    public static AppCenter getInstance() {
        if (instance == null) {
            instance = new AppCenter();
        }

        return instance;
    }

    /**
     * @param username the username to search for
     * @return the found User or null
     */
    public User getUserByUsername(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static final Comparator<Milestone> MILESTONE_COMPARATOR =
            Comparator.comparing(Milestone::getDueDate)
                    .thenComparing(Milestone::getName);

    /**
     * @param milestone the milestone to add
     */
    public void addMilestone(final Milestone milestone) {
        milestones.add(milestone);
        milestones.sort(MILESTONE_COMPARATOR);
    }

    /**
     * @param ticket the ticket to evaluate
     * @return list of required expertise areas
     */
    public ArrayList<ExpertiseArea> requiredExpertiseAreas(final Ticket ticket) {
        ArrayList<ExpertiseArea> expertiseAreas = new ArrayList<>();
        expertiseAreas.add(ticket.getExpertiseArea());
        expertiseAreas.add(ExpertiseArea.FULLSTACK);
        if (ticket.getExpertiseArea() == ExpertiseArea.DESIGN) {
            expertiseAreas.add(ExpertiseArea.FRONTEND);
        }
        if (ticket.getExpertiseArea() == ExpertiseArea.FRONTEND) {
            expertiseAreas.add(ExpertiseArea.DESIGN);
        }
        if (ticket.getExpertiseArea() == ExpertiseArea.DB) {
            expertiseAreas.add(ExpertiseArea.BACKEND);
        }
        expertiseAreas.sort(Comparator.comparing(Enum::name));
        return expertiseAreas;
    }

    /**
     * @param ticket the ticket to evaluate
     * @return list of required seniority levels
     */
    public ArrayList<String> requiredSeniority(final Ticket ticket) {
        ArrayList<String> seniorities = new ArrayList<>();
        boolean isLowOrMid = ticket.getBusinessPriority() == BusinessPriority.LOW
                || ticket.getBusinessPriority() == BusinessPriority.MEDIUM;

        if (isLowOrMid) {
            seniorities.add("JUNIOR");
            seniorities.add("MID");
            seniorities.add("SENIOR");
        } else if (ticket.getBusinessPriority() == BusinessPriority.HIGH) {
            seniorities.add("MID");
            seniorities.add("SENIOR");
        } else {
            seniorities.add("SENIOR");
        }
        Collections.sort(seniorities);
        return seniorities;
    }

    /**
     * @param ticketID the ID of the ticket
     * @return the milestone containing the ticket, or null
     */
    public Milestone getMilestoneByTicketID(final Integer ticketID) {
        for (Milestone milestone : milestones) {
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getId() == ticketID) {
                    return milestone;
                }
            }
        }
        return null;
    }
}
