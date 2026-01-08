package commands;

import Milestones.Milestone;
import Milestones.Repartition;
import Tickets.Comment;
import Tickets.Ticket;
import enums.TicketStatus;
import fileio.CommandInput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CommandHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectNode createTicketNode(Ticket ticket) {
        ObjectNode ticketNode = MAPPER.createObjectNode();
        ticketNode.put("id", ticket.getId());
        ticketNode.put("type", ticket.getType() != null ? ticket.getType().toString() : "");
        ticketNode.put("title", ticket.getTitle() != null ? ticket.getTitle() : "");
        ticketNode.put("businessPriority", ticket.getBusinessPriority() != null ? ticket.getBusinessPriority().toString() : "");
        ticketNode.put("status", ticket.getStatus() != null ? ticket.getStatus().toString() : "");
        ticketNode.put("createdAt", ticket.getCreatedAt() != null ? ticket.getCreatedAt() : "");
        ticketNode.put("assignedAt", ticket.getAssignedAt() != null ? ticket.getAssignedAt() : "");
        ticketNode.put("solvedAt", ticket.getSolvedAt() != null ? ticket.getSolvedAt() : "");
        ticketNode.put("assignedTo", ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "");
        ticketNode.put("reportedBy", ticket.getReportedBy() != null ? ticket.getReportedBy() : "");
        
        ArrayNode commentsArray = MAPPER.createArrayNode();
        if (ticket.getComments() != null) {
            for (Comment comment : ticket.getComments()) {
                ObjectNode commentNode = MAPPER.createObjectNode();
                commentNode.put("author", comment.getAuthor() != null ? comment.getAuthor() : "");
                commentNode.put("content", comment.getContent() != null ? comment.getContent() : "");
                commentNode.put("createdAt", comment.getCreatedAt() != null ? comment.getCreatedAt() : "");
                commentsArray.add(commentNode);
            }
        }
        ticketNode.set("comments", commentsArray);
        
        return ticketNode;
    }

    public static ObjectNode createMilestoneNode(Milestone milestone, CommandInput command) {
        ObjectNode milestoneNode = MAPPER.createObjectNode();

        milestoneNode.put("name", milestone.getName() != null ? milestone.getName() : "");

        ArrayNode blockingForArray = MAPPER.createArrayNode();
        if (milestone.getBlockingFor() != null) {
            for (Milestone block : milestone.getBlockingFor()) {
                blockingForArray.add(block.getName() != null ? block.getName() : "");
            }
        }
        milestoneNode.set("blockingFor", blockingForArray);

        milestoneNode.put("dueDate", milestone.getDueDate() != null ? milestone.getDueDate().toString() : "");
        milestoneNode.put("createdAt", milestone.getCreatedAt() != null ? milestone.getCreatedAt().toString() : "");

        ArrayNode ticketsArray = MAPPER.createArrayNode();
        if (milestone.getTickets() != null) {
            for (Ticket ticket : milestone.getTickets()) {
                ticketsArray.add(ticket.getId());
            }
        }
        milestoneNode.set("tickets", ticketsArray);

        ArrayNode assignedDevsArray = MAPPER.createArrayNode();
        if (milestone.getAssignedDevs() != null) {
            for (String dev : milestone.getAssignedDevs()) {
                assignedDevsArray.add(dev != null ? dev : "");
            }
        }
        milestoneNode.set("assignedDevs", assignedDevsArray);

        milestoneNode.put("createdBy", milestone.getCreatedBy() != null ? milestone.getCreatedBy() : "");
        milestoneNode.put("status", milestone.getStatus() != null ? milestone.getStatus().toString() : "");
        milestoneNode.put("isBlocked", milestone.getBlocked());
        milestoneNode.put("daysUntilDue", milestone.getDaysUntilDue(command.getTimestamp()));
        milestoneNode.put("overdueBy", milestone.getOverdueBy(command.getTimestamp()));

        ArrayNode openTicketsArray = MAPPER.createArrayNode();
        if (milestone.getTickets() != null) {
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() == TicketStatus.OPEN) {
                    openTicketsArray.add(ticket.getId());
                }
            }
        }
        milestoneNode.set("openTickets", openTicketsArray);

        ArrayNode closedTicketsArray = MAPPER.createArrayNode();
        if (milestone.getTickets() != null) {
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() == TicketStatus.CLOSED) {
                    closedTicketsArray.add(ticket.getId());
                }
            }
        }
        milestoneNode.set("closedTickets", closedTicketsArray);

        milestoneNode.put("completionPercentage", milestone.getCompletionPercentage());

        ArrayNode repartitionArray = MAPPER.createArrayNode();
        if (milestone.getRepartitions() != null) {
            for (Repartition repartition : milestone.getRepartitions()) {
                ObjectNode repartitionNode = MAPPER.createObjectNode();
                repartitionNode.put("developer", repartition.getDeveloper() != null ? repartition.getDeveloper().getUsername() : "");

                ArrayNode assignedTicketsArray = MAPPER.createArrayNode();
                if (repartition.getTickets() != null) {
                    for (Ticket ticket : repartition.getTickets()) {
                        assignedTicketsArray.add(ticket.getId());
                    }
                }

                repartitionNode.set("assignedTickets", assignedTicketsArray);
                repartitionArray.add(repartitionNode);
            }
        }
        milestoneNode.set("repartition", repartitionArray);

        return milestoneNode;

    }
}
