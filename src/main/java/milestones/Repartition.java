package milestones;

import tickets.Ticket;
import users.Developer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Repartition {
    private Developer developer;
    private List<Ticket> tickets;

    public Repartition() {
        tickets = new ArrayList<>();
    }
    public Repartition(Developer developer) {
        this.developer = developer;
    }
}
