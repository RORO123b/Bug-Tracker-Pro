package Tickets;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;

public abstract class TicketBuilder<T extends Ticket, B extends TicketBuilder<T, B>> {

    protected T ticket;

    protected abstract B self();

    public B id(int id) {
        ticket.id = id;
        return self();
    }

    public B title(String title) {
        ticket.title = title;
        return self();
    }

    public B type(TicketType type) {
        ticket.type = type;
        return self();
    }

    public B businessPriority(BusinessPriority priority) {
        if (ticket.reportedBy.equals("")) {
            ticket.businessPriority = BusinessPriority.LOW;
            return self();
        }
        ticket.businessPriority = priority;
        return self();
    }

    public B status(TicketStatus status) {
        ticket.status = status;
        return self();
    }

    public B expertiseArea(ExpertiseArea area) {
        ticket.expertiseArea = area;
        return self();
    }

    public B description(String description) {
        ticket.description = description;
        return self();
    }

    public B reportedBy(String reportedBy) {
        ticket.reportedBy = reportedBy;
        return self();
    }

    public B createdAt(String createdAt) {
        ticket.setCreatedAt(createdAt);
        return self();
    }

    public abstract T build();
}

