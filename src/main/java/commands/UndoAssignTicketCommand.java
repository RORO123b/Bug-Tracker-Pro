package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;
import tickets.action.ActionBuilder;
import users.Developer;

public final class UndoAssignTicketCommand implements Command {
    public UndoAssignTicketCommand() { }

    /**
     * Executes the undo assignment command
     * @param mapper the object mapper for JSON creation
     * @param command the input command details
     * @return the result node or null
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            Developer dev = (Developer) appCenter.getUserByUsername(command.getUsername());
            Ticket ticket = appCenter.getTickets().get(command.getTicketID());

            if (ticket.getStatus() !=  TicketStatus.IN_PROGRESS) {
                throw new IllegalStateException("Only IN_PROGRESS tickets can be unassigned.");
            }

            dev.removeTicket(ticket);
            ticket.setStatus(TicketStatus.OPEN);
            ticket.setAssignedAt("");
            ticket.setAssignedTo("");
            ticket.getHistory().add(new ActionBuilder()
                    .action("DE-ASSIGNED")
                    .by(command.getUsername())
                    .timestamp(command.getTimestamp())
                    .build());
            return null;
        } catch (IllegalStateException e) {
            ObjectNode error = mapper.createObjectNode();
            error.put("command", command.getCommand());
            error.put("username", command.getUsername());
            error.put("timestamp", command.getTimestamp().toString());
            error.put("error", e.getMessage());
            return error;
        }
    }
}
