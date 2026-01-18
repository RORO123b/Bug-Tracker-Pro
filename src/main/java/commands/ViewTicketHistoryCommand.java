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
    private ObjectMapper mapper;

    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        this.mapper = mapper;
        AppCenter appCenter = AppCenter.getInstance();
        User user = appCenter.getUserByUsername(command.getUsername());

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", user.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        if (user.getRole().equals("REPORTER")) {
            commandNode.put("error", "The user does not have permission to execute this command: "
                    + "required role DEVELOPER, MANAGER; user role REPORTER.");
            return commandNode;
        }

        ArrayNode history = mapper.createArrayNode();

        if (user.getRole().equals("MANAGER")) {
            addManagerTicketHistory(history, user, appCenter);
        } else if (user.getRole().equals("DEVELOPER")) {
            addDeveloperTicketHistory(history, user, appCenter);
        }

        commandNode.put("ticketHistory", history);
        return commandNode;
    }

    private void addManagerTicketHistory(final ArrayNode history, final User user,
                                         final AppCenter appCenter) {
        for (Milestone milestone : appCenter.getMilestones()) {
            if (!milestone.getCreatedBy().equals(user.getUsername())) {
                continue;
            }
    
            for (Ticket ticket : milestone.getTickets()) {
                ObjectNode ticketNode = createTicketNode(ticket, false);
                history.add(ticketNode);
            }
        }
    }

    private void addDeveloperTicketHistory(final ArrayNode history, final User user,
                                           final AppCenter appCenter) {
        for (Ticket ticket : appCenter.getTickets()) {
            if (ticket.getAssignedTo() == null) {
                continue;
            }
    
            if (!ticket.getAssignedTo().equals(user.getUsername())
                    && !ticket.checkPastDevs(user.getUsername())) {
                continue;
            }
    
            ObjectNode ticketNode = createTicketNode(ticket, true);
            history.add(ticketNode);
        }
    }

    private ObjectNode createTicketNode(final Ticket ticket, 
                                        final boolean stopAtDeassign) {
        ObjectNode ticketNode = mapper.createObjectNode();
        ticketNode.put("id", ticket.getId());
        ticketNode.put("title", ticket.getTitle());
        ticketNode.put("status", ticket.getStatus().toString());

        ArrayNode historyNode = createActionsArray(ticket, stopAtDeassign);
        ticketNode.put("actions", historyNode);

        ArrayNode commentsArray = createCommentsArray(ticket);
        ticketNode.set("comments", commentsArray);

        return ticketNode;
    }

    private ArrayNode createActionsArray(final Ticket ticket, final boolean stopAtDeassign) {
        ArrayNode historyNode = mapper.createArrayNode();

        for (Action action : ticket.getHistory()) {
            ObjectNode actionNode = CommandHelper.createActionNode(action);
            historyNode.add(actionNode);
    
            if (stopAtDeassign && action.getAction().equals("DE-ASSIGNED")) {
                break;
            }
        }

        return historyNode;
    }

    private ArrayNode createCommentsArray(final Ticket ticket) {
        ArrayNode commentsArray = mapper.createArrayNode();

        if (ticket.getComments() == null) {
            return commentsArray;
        }

        for (Comment comment : ticket.getComments()) {
            ObjectNode commentNode = createCommentNode(comment);
            commentsArray.add(commentNode);
        }

        return commentsArray;
    }

    private ObjectNode createCommentNode(final Comment comment) {
        ObjectNode commentNode = mapper.createObjectNode();
        commentNode.put("author", comment.getAuthor() != null ? comment.getAuthor() : "");
        commentNode.put("content", comment.getContent() != null ? comment.getContent() : "");
        commentNode.put("createdAt", comment.getCreatedAt() != null 
            ? comment.getCreatedAt().toString() : "");
        return commentNode;
    }
}
