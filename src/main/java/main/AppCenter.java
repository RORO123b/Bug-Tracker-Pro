package main;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.Phases;
import enums.TicketStatus;
import enums.TicketType;
import milestones.Milestone;
import tickets.Ticket;
import users.Developer;
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
    private static final double PERCENT = 100.0;
    private static final int RISK_NEGLIGIBLE = 24;
    private static final int RISK_MODERATE = 49;
    private static final int RISK_SIGNIFICANT = 74;
    private static final int AVERAGE_IMPACT = 50;
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

    private void checkTransition(final LocalDate currentDate) {
        if (currentPeriod == Phases.TESTING
            && (int) ChronoUnit.DAYS.between(datePeriodStart, currentDate)
                + 1 > TESTING_PHASE_DURATION) {
            currentPeriod = Phases.DEVELOPMENT;
            datePeriodStart = currentDate;
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

    /**
     * Gets all tickets that are either OPEN or IN_PROGRESS
     * @return list of open or in-progress tickets
     */
    public List<Ticket> getOpenInProgressTickets() {
        List<Ticket> openInProgressTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.OPEN
                    ||  ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                openInProgressTickets.add(ticket);
            }
        }

        return openInProgressTickets;
    }

    /**
     * Gets all tickets that are either RESOLVED or CLOSED
     * @return list of resolved or closed tickets
     */
    public List<Ticket> getResolvedClosedTickets() {
        List<Ticket> resolvedClosedTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.RESOLVED || ticket.getStatus() == TicketStatus.CLOSED) {
                resolvedClosedTickets.add(ticket);
            }
        }

        return resolvedClosedTickets;
    }

    /**
     * Filters tickets by type
     * @param type the ticket type to filter by
     * @param ticketList the list of tickets to filter
     * @return filtered list of tickets matching the type
     */
    public List<Ticket> getTicketsByType(final TicketType type,
                                          final List<Ticket> ticketList) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket.getType().equals(type)) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Filters tickets by priority
     * @param priority the business priority to filter by
     * @param ticketList the list of tickets to filter
     * @return filtered list of tickets matching the priority
     */
    public List<Ticket> getTicketsByPriority(final BusinessPriority priority,
                                              final List<Ticket> ticketList) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket.getBusinessPriority() == priority) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Calculates average impact score for a list of tickets
     * @param ticketList the list of tickets to calculate average for
     * @return the average impact score
     */
    public double calculateAverageImpact(final List<Ticket> ticketList) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            scores.add(ticket.calculateImpactFinal());
        }
        return Math.round(scores.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0.0) * PERCENT) / PERCENT;
    }

    /**
     * Calculates average risk category for a list of tickets
     * @param ticketList the list of tickets to calculate average risk for
     * @return the risk category as a string
     */
    public String calculateAverageRisk(final List<Ticket> ticketList) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            scores.add(ticket.calculateRiskFinal());
        }
        double res = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        if (res < RISK_NEGLIGIBLE) {
            return "NEGLIGIBLE";
        } else if (res < RISK_MODERATE) {
            return "MODERATE";
        } else if (res < RISK_SIGNIFICANT) {
            return "SIGNIFICANT";
        }
        return "MAJOR";
    }

    public Double calculateEfficiency(final List<Ticket> ticketList) {
        List<Double> scores = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            scores.add(ticket.calculateEfficiencyFinal());
        }
        return Math.round(scores.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0.0) * PERCENT) / PERCENT;
    }

    /**
     * Determines the overall application stability based on ticket metrics
     * @return the stability status as a string
     */
    public String getAppStability() {
        List<Ticket> openInProgress = getOpenInProgressTickets();

        if (openInProgress.isEmpty()) {
            return "STABLE";
        }

        if (allTypesHaveNegligibleRisk(openInProgress) 
                && allTypesHaveLowImpact(openInProgress)) {
            return "STABLE";
        }

        if (anyTypeHasSignificantRisk(openInProgress)) {
            return "UNSTABLE";
        }

        return "PARTIALLY STABLE";
    }

    private boolean allTypesHaveNegligibleRisk(final List<Ticket> tickets) {
        TicketType[] types = {TicketType.BUG, TicketType.FEATURE_REQUEST, TicketType.UI_FEEDBACK};

        for (TicketType type : types) {
            String risk = calculateAverageRisk(getTicketsByType(type, tickets));
            if (!risk.equals("NEGLIGIBLE")) {
                return false;
            }
        }

        return true;
    }

    private boolean allTypesHaveLowImpact(final List<Ticket> tickets) {
        TicketType[] types = {TicketType.BUG, TicketType.FEATURE_REQUEST, TicketType.UI_FEEDBACK};

        for (TicketType type : types) {
            double impact = calculateAverageImpact(getTicketsByType(type, tickets));
            if (impact >= AVERAGE_IMPACT) {
                return false;
            }
        }

        return true;
    }

    private boolean anyTypeHasSignificantRisk(final List<Ticket> tickets) {
        TicketType[] types = {TicketType.BUG, TicketType.FEATURE_REQUEST, TicketType.UI_FEEDBACK};

        for (TicketType type : types) {
            String risk = calculateAverageRisk(getTicketsByType(type, tickets));
            if (risk.equals("SIGNIFICANT")) {
                return true;
            }
        }

        return false;
    }

    public List<Ticket> getOpenTicketsForDeveloper(final Developer dev) {
        List<Ticket> openTickets = new ArrayList<>();

        for (Milestone milestone : milestones) {
            if (!milestone.getAssignedDevs().contains(dev.getUsername())) {
                continue;
            }

            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() == TicketStatus.OPEN) {
                    openTickets.add(ticket);
                }
            }
        }
        return openTickets;
    }
}
