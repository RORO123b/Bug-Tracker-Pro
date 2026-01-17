package main;

import enums.*;
import milestones.Milestone;
import tickets.Ticket;
import users.User;
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
                    + 1 > TESTING_PHASE_DURATION) {
                currentPeriod = Phases.DEVELOPMENT;
                datePeriodStart = currentDate;
            }
        }
    }

    private void updateMilestones(final LocalDate currentDate) {
        for (Milestone milestone : milestones) {
            milestone.checkBusinessPriority(currentDate);
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

    /**
     * @param ticketId the ID of the ticket to search for
     * @return the found Ticket or null
     */
    public Ticket getTicketById(final int ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == ticketId) {
                return ticket;
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

    public List<Ticket> getOpenInProgressTickets() {
        List<Ticket> openInProgressTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.OPEN ||  ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                openInProgressTickets.add(ticket);
            }
        }

        return openInProgressTickets;
    }

    public List<Ticket> getResolvedClosedTickets() {
        List<Ticket> resolvedClosedTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.RESOLVED ||  ticket.getStatus() == TicketStatus.CLOSED) {
                resolvedClosedTickets.add(ticket);
            }
        }

        return resolvedClosedTickets;
    }

    public List<Ticket> getTicketsByType(final TicketType type, final List<Ticket> tickets) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getType().equals(type)) {
                result.add(ticket);
            }
        }
        return result;
    }

    public List<Ticket> getTicketsByPriority(final BusinessPriority priority, final List<Ticket> tickets) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getBusinessPriority() == priority) {
                result.add(ticket);
            }
        }
        return result;
    }

    public double calculateAverageImpact(List<Ticket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : tickets) {
            scores.add(ticket.calculateImpactFinal());
        }
        return Math.round(scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0) / 100.0;
    }

    public String calculateAverageRisk(List<Ticket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : tickets) {
            scores.add(ticket.calculateRiskFinal());
        }
        double res = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        if (res < 24) {
            return "NEGLIGIBLE";
        } else if (res < 49) {
            return "MODERATE";
        } else if (res < 74) {
            return "SIGNIFICANT";
        }
        return "MAJOR";
    }

    public Double calculateEfficiency(List<Ticket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : tickets) {
            scores.add(ticket.calculateEfficiencyFinal());
        }
        return Math.round(scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0) / 100.0;
    }

    public String getAppStability() {
        if (getOpenInProgressTickets().isEmpty()) {
            return "STABLE";
        }

        if (calculateAverageRisk(getTicketsByType(TicketType.BUG, getOpenInProgressTickets())).equals("NEGLIGIBLE")
        && calculateAverageRisk(getTicketsByType(TicketType.FEATURE_REQUEST, getOpenInProgressTickets())).equals("NEGLIGIBLE")
        && calculateAverageRisk(getTicketsByType(TicketType.UI_FEEDBACK, getOpenInProgressTickets())).equals("NEGLIGIBLE")
        && calculateAverageImpact(getTicketsByType(TicketType.BUG, getOpenInProgressTickets())) < 50
        && calculateAverageImpact(getTicketsByType(TicketType.FEATURE_REQUEST, getOpenInProgressTickets())) < 50
        && calculateAverageImpact(getTicketsByType(TicketType.UI_FEEDBACK, getOpenInProgressTickets())) < 50) {
            return "STABLE";
        }

        if (calculateAverageRisk(getTicketsByType(TicketType.BUG, getOpenInProgressTickets())).equals("SIGNIFICANT")
            || calculateAverageRisk(getTicketsByType(TicketType.FEATURE_REQUEST, getOpenInProgressTickets())).equals("SIGNIFICANT")
            || calculateAverageRisk(getTicketsByType(TicketType.UI_FEEDBACK, getOpenInProgressTickets())).equals("SIGNIFICANT")) {
            return "UNSTABLE";
        }

        return "PARTIALLY STABLE";
    }
}
