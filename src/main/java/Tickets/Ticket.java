package Tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Ticket {
    protected int id;
    protected TicketType type;
    protected String title;
    protected BusinessPriority businessPriority;
    protected TicketStatus status;
    protected ExpertiseArea expertiseArea;
    protected String description;
    protected String reportedBy;
    protected String createdAt;
    protected String solvedAt;
    protected String assignedAt;
    protected String assignedTo;
    protected List<Comment> comments;
}
