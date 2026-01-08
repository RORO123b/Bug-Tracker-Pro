package commands;

import Tickets.Comment;
import Tickets.Ticket;
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
}
