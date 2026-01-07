package Tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;

public abstract class Ticket {
    protected int id;
    protected TicketType type;
    protected String title;
    protected BusinessPriority businessPriority;
    protected TicketStatus status;
    protected ExpertiseArea expertiseArea;
    protected String description;
    protected String reportedBy;
}
