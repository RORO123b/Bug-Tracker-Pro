package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Comment;
import tickets.Ticket;
import users.Developer;
import users.User;

public final class AddCommentCommand implements Command {
    public AddCommentCommand() { }

    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        final int minCommentLength = 10;
        try {
            AppCenter appCenter = AppCenter.getInstance();
            Ticket ticket;
            try {
                ticket = appCenter.getTickets().get(command.getTicketID());
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
            User user = appCenter.getUserByUsername(command.getUsername());
            if (ticket.getReportedBy().isEmpty()) {
                throw new IllegalArgumentException("Comments are not allowed on anonymous tickets.");
            }
            if (user.getRole().equals("REPORTER")
                    && ticket.getStatus().equals(TicketStatus.CLOSED)) {
                throw new IllegalArgumentException(
                        "Reporters cannot comment on CLOSED tickets.");
            }
            if (command.getComment().length() < minCommentLength) {
                throw new IllegalArgumentException(
                        "Comment must be at least 10 characters long.");
            }
            if (user.getRole().equals("DEVELOPER")
                    && !((Developer) user).getAssignedTickets().contains(ticket)) {
                throw new IllegalArgumentException("Ticket " + command.getTicketID()
                        + " is not assigned to the developer " + user.getUsername() + ".");
            }
            if (user.getRole().equals("REPORTER")
                    && !ticket.getReportedBy().equals(command.getUsername())) {
                throw new IllegalArgumentException("Reporter " + user.getUsername()
                        + " cannot comment on ticket " + command.getTicketID() + ".");
            }
            ticket.getComments().add(new Comment(user.getUsername(), command.getComment(),
                    command.getTimestamp()));
        } catch (IllegalArgumentException e) {
            ObjectNode error = mapper.createObjectNode();
            error.put("command", command.getCommand());
            error.put("username", command.getUsername());
            error.put("timestamp", command.getTimestamp().toString());
            error.put("error", e.getMessage());
            return error;
        }
        return null;
    }
}
