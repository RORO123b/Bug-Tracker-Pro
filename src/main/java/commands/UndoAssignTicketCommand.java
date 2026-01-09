package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;
import users.Developer;

public class UndoAssignTicketCommand implements Command {
    public UndoAssignTicketCommand() {}

    @Override
    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
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
