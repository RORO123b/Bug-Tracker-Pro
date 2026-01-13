package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import milestones.Milestone;
import tickets.Comment;
import tickets.action.Action;
import tickets.Ticket;
import users.User;

public final class ViewTicketHistoryCommand implements Command {
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        AppCenter appCenter = AppCenter.getInstance();
        User user = appCenter.getUserByUsername(command.getUsername());
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", user.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());
        ArrayNode history = mapper.createArrayNode();
        if (user.getRole().equals("MANAGER")) {
            for (Milestone milestone : appCenter.getMilestones()) {
                if (milestone.getCreatedBy().equals(user.getUsername())) {
                    for (Ticket ticket : milestone.getTickets()) {
                        ObjectNode ticketNode = mapper.createObjectNode();
                        ticketNode.put("id", ticket.getId());
                        ticketNode.put("title", ticket.getTitle());
                        ticketNode.put("status", ticket.getStatus().toString());
                        ArrayNode historyNode = mapper.createArrayNode();
                        for (Action action : ticket.getHistory()) {
                            ObjectNode actionNode = CommandHelper.createActionNode(action);
                            historyNode.add(actionNode);
                        }
                        ticketNode.put("actions", historyNode);
                        ArrayNode commentsArray = mapper.createArrayNode();
                        if (ticket.getComments() != null) {
                            for (Comment comment : ticket.getComments()) {
                                ObjectNode commentNode = mapper.createObjectNode();
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
                        history.add(ticketNode);
                    }
                }
            }
        }
        if (user.getRole().equals("DEVELOPER")) {
            for (Ticket ticket : appCenter.getTickets()) {
                if (ticket.getAssignedTo() == null) {
                    continue;
                }
                if (ticket.getAssignedTo().equals(user.getUsername())
                        || ticket.checkPastDevs(user.getUsername())) {
                    ObjectNode ticketNode = mapper.createObjectNode();
                    ticketNode.put("id", ticket.getId());
                    ticketNode.put("title", ticket.getTitle());
                    ticketNode.put("status", ticket.getStatus().toString());
                    ArrayNode historyNode = mapper.createArrayNode();
                    for (Action action : ticket.getHistory()) {
                        ObjectNode actionNode = CommandHelper.createActionNode(action);
                        historyNode.add(actionNode);
                        if (action.getAction().equals("DE-ASSIGNED")) {
                            break;
                        }
                    }
                    ticketNode.put("actions", historyNode);
                    ArrayNode commentsArray = mapper.createArrayNode();
                    if (ticket.getComments() != null) {
                        for (Comment comment : ticket.getComments()) {
                            ObjectNode commentNode = mapper.createObjectNode();
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
                    history.add(ticketNode);
                }
            }
        }
        commandNode.put("ticketHistory", history);
        return commandNode;
    }
}
