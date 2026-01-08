package main;

import Milestones.Milestone;
import Tickets.Ticket;
import Users.User;
import enums.Phases;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class AppCenter {
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

    private void checkTransition(LocalDate currentDate) {
        if (currentPeriod == Phases.TESTING) {
            if ((int) ChronoUnit.DAYS.between(datePeriodStart, currentDate) + 1 >= 12) {
                currentPeriod = Phases.DEVELOPMENT;
                datePeriodStart = currentDate;
            }
        } 
    }

    private void checkBusinessPriority(LocalDate currentDate) {
        for (Milestone milestone : milestones) {
            milestone.checkBusinessPriority(currentDate);
        }
    }

    public void updates(LocalDate currentDate) {
        checkTransition(currentDate);
        checkBusinessPriority(currentDate);
    }
    
    public static void resetInstance() {
        instance = null;
    }
    
    public static AppCenter getInstance() {
        if (instance == null) {
            instance = new AppCenter();
        }

        return instance;
    }

    public User getUserByUsername(String username) {
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

    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
        milestones.sort(MILESTONE_COMPARATOR);
    }

}
