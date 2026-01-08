package main;

import Tickets.Ticket;
import Users.User;
import enums.Phases;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppCenter {
    private static AppCenter instance;

    private Phases currentPeriod;
    private LocalDate datePeriodStart;
    private List<Ticket> tickets;
    private List<User> users;
    private AppCenter() {
        currentPeriod = Phases.TESTING;
        tickets = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void checkTransition(LocalDate currentDate) {
        if (currentPeriod == Phases.TESTING) {
            if (currentDate.isAfter(datePeriodStart.plusDays(12))) {
                currentPeriod = Phases.DEVELOPMENT;
                datePeriodStart = currentDate;
                tickets = new ArrayList<>();
            }
        } 
    }
    public static AppCenter getInstance() {
        if (instance == null) {
            instance = new AppCenter();
        }

        return instance;
    }
}
