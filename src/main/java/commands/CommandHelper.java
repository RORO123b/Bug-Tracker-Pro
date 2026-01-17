package commands;

import main.AppCenter;
import milestones.Milestone;
import tickets.action.Action;
import tickets.Comment;
import tickets.Ticket;
import enums.TicketStatus;
import fileio.CommandInput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import users.Developer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CommandHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private CommandHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates an ObjectNode for a Ticket.
     * @param ticket Ticket
     * @return ObjectNode for output
     */
    public static ObjectNode createTicketNode(final Ticket ticket) {
        ObjectNode ticketNode = MAPPER.createObjectNode();
        ticketNode.put("id", ticket.getId());
        ticketNode.put("type", ticket.getType() != null ? ticket.getType().toString() : "");
        ticketNode.put("title", ticket.getTitle() != null ? ticket.getTitle() : "");
        ticketNode.put("businessPriority", ticket.getBusinessPriority() != null
                ? ticket.getBusinessPriority().toString() : "");
        ticketNode.put("status", ticket.getStatus() != null ? ticket.getStatus().toString() : "");
        ticketNode.put("createdAt", ticket.getCreatedAt() != null ? ticket.getCreatedAt() : "");
        ticketNode.put("assignedAt", ticket.getAssignedAt() != null ? ticket.getAssignedAt().toString() : "");
        ticketNode.put("solvedAt", ticket.getSolvedAt() != null ? ticket.getSolvedAt().toString() : "");
        ticketNode.put("assignedTo", ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "");
        ticketNode.put("reportedBy", ticket.getReportedBy() != null ? ticket.getReportedBy() : "");

        ArrayNode commentsArray = MAPPER.createArrayNode();
        if (ticket.getComments() != null) {
            for (Comment comment : ticket.getComments()) {
                ObjectNode commentNode = MAPPER.createObjectNode();
                commentNode.put("author", comment.getAuthor() != null
                        ? comment.getAuthor() : "");
                commentNode.put("content", comment.getContent() != null
                        ? comment.getContent() : "");
                commentNode.put("createdAt", comment.getCreatedAt() != null
                        ? comment.getCreatedAt().toString() : "");
                commentsArray.add(commentNode);
            }
        }
        ticketNode.set("comments", commentsArray);

        return ticketNode;
    }

    /**
     * Creates an ObjectNode for a Ticket.
     * @param ticket Ticket
     * @return ObjectNode for output
     */
    public static ObjectNode createAssignedTicketNode(final Ticket ticket) {
        ObjectNode ticketNode = MAPPER.createObjectNode();
        ticketNode.put("id", ticket.getId());
        ticketNode.put("type", ticket.getType() != null ? ticket.getType().toString() : "");
        ticketNode.put("title", ticket.getTitle() != null ? ticket.getTitle() : "");
        ticketNode.put("businessPriority", ticket.getBusinessPriority() != null
                ? ticket.getBusinessPriority().toString() : "");
        ticketNode.put("status", ticket.getStatus() != null ? ticket.getStatus().toString() : "");
        ticketNode.put("createdAt", ticket.getCreatedAt() != null ? ticket.getCreatedAt() : "");
        ticketNode.put("assignedAt", ticket.getAssignedAt() != null ? ticket.getAssignedAt().toString() : "");
        ticketNode.put("reportedBy", ticket.getReportedBy() != null ? ticket.getReportedBy() : "");

        ArrayNode commentsArray = MAPPER.createArrayNode();
        if (ticket.getComments() != null) {
            for (Comment comment : ticket.getComments()) {
                ObjectNode commentNode = MAPPER.createObjectNode();
                commentNode.put("author", comment.getAuthor() != null
                        ? comment.getAuthor() : "");
                commentNode.put("content", comment.getContent() != null
                        ? comment.getContent() : "");
                commentNode.put("createdAt", comment.getCreatedAt() != null
                        ? comment.getCreatedAt().toString() : "");
                commentsArray.add(commentNode);
            }
        }
        ticketNode.set("comments", commentsArray);

        return ticketNode;
    }

    /**
     * Creates an ObjectNode for a Milestone.
     * @param milestone
     * @param command
     * @return ObjectNode for output
     */
    public static ObjectNode createMilestoneNode(final Milestone milestone,
                                                 final CommandInput command) {
        ObjectNode milestoneNode = MAPPER.createObjectNode();

        milestoneNode.put("name", milestone.getName() != null ? milestone.getName() : "");

        ArrayNode blockingForArray = MAPPER.createArrayNode();
        if (milestone.getBlockingFor() != null) {
            for (Milestone block : milestone.getBlockingFor()) {
                blockingForArray.add(block.getName() != null ? block.getName() : "");
            }
        }
        milestoneNode.set("blockingFor", blockingForArray);

        milestoneNode.put("dueDate", milestone.getDueDate() != null
                ? milestone.getDueDate().toString() : "");
        milestoneNode.put("createdAt", milestone.getCreatedAt() != null
                ? milestone.getCreatedAt().toString() : "");

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

        milestoneNode.put("createdBy", milestone.getCreatedBy() != null
                ? milestone.getCreatedBy() : "");
        milestoneNode.put("status", milestone.getStatus() != null
                ? milestone.getStatus() : "");
        milestoneNode.put("isBlocked", milestone.getBlocked());
        milestoneNode.put("daysUntilDue", milestone.getDaysUntilDue());
        milestoneNode.put("overdueBy", milestone.getOverdueBy());

        ArrayNode openTicketsArray = MAPPER.createArrayNode();
        if (milestone.getTickets() != null) {
            for (Ticket ticket : milestone.getTickets()) {
                if (ticket.getStatus() != TicketStatus.CLOSED) {
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
        AppCenter appCenter = AppCenter.getInstance();

        List<String> sortedDevs = new ArrayList<>(milestone.getAssignedDevs());
        sortedDevs.sort((dev1, dev2) -> {
            Developer d1 = (Developer) appCenter.getUserByUsername(dev1);
            Developer d2 = (Developer) appCenter.getUserByUsername(dev2);
            int count1 = 0;
            int count2 = 0;
            for (Ticket t : d1.getAssignedTickets()) {
                if (milestone.getTickets().contains(t)) {
                    count1++;
                }
            }
            for (Ticket t : d2.getAssignedTickets()) {
                if (milestone.getTickets().contains(t)) {
                    count2++;
                }
            }
            if (count1 != count2) {
                return Integer.compare(count1, count2);
            }
            return dev1.compareTo(dev2);
        });

        for (String username : sortedDevs) {
            Developer dev = (Developer) appCenter.getUserByUsername(username);
            ObjectNode repartitionNode = MAPPER.createObjectNode();
            repartitionNode.put("developer", dev != null
                    ? dev.getUsername() : "");

            ArrayNode assignedTicketsArray = MAPPER.createArrayNode();
            List<Integer> ticketIds = new ArrayList<>();
            for (Ticket ticket : dev.getAssignedTickets()) {
                if (milestone.getTickets().contains(ticket)) {
                    ticketIds.add(ticket.getId());
                }
            }
            Collections.sort(ticketIds);
            for (Integer ticketId : ticketIds) {
                assignedTicketsArray.add(ticketId);
            }
            repartitionNode.set("assignedTickets", assignedTicketsArray);
            repartitionArray.add(repartitionNode);
        }
        milestoneNode.set("repartition", repartitionArray);

        return milestoneNode;
    }

    /**
     * Creates an ObjectNode representation of an action
     * @param action the action to convert
     * @return an ObjectNode containing action details
     */
    public static ObjectNode createActionNode(final Action action) {
        ObjectNode actionNode = MAPPER.createObjectNode();

        if (action.getAction().equals("STATUS_CHANGED")) {
            actionNode.put("from", action.getOldStatus());
            actionNode.put("to", action.getNewStatus());
            actionNode.put("by", action.getBy());
        }

        if (action.getAction().equals("ASSIGNED") || action.getAction().equals("DE-ASSIGNED")) {
            actionNode.put("by", action.getBy());
        }

        if (action.getAction().equals("ADDED_TO_MILESTONE")) {
            actionNode.put("milestone", action.getMilestone());
            actionNode.put("by", action.getBy());
        }

        if (action.getAction().equals("REMOVED_FROM_DEV")) {
            actionNode.put("by", action.getBy());
            actionNode.put("from", action.getRemovedDev());
        }

        actionNode.put("timestamp", action.getTimestamp().toString());
        actionNode.put("action", action.getAction());
        return actionNode;
    }

}
