package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;

public final class UndoAddCommentCommand implements Command {
    public UndoAddCommentCommand() { }

    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            Ticket ticket = appCenter.getTicketById(command.getTicketID());
            if (ticket == null) {
                return null;
            }
            if (ticket.getReportedBy().isEmpty()) {
                throw new IllegalArgumentException(
                        "Comments are not allowed on anonymous tickets.");
            }
            if (!ticket.getComments().isEmpty()) {
                ticket.getComments().removeLast();
            }
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
