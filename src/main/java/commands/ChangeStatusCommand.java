package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;
import tickets.action.ActionBuilder;
import users.Developer;

public class ChangeStatusCommand implements Command {
    public ChangeStatusCommand() { }

    @Override
    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            Ticket ticket = appCenter.getTickets().get(command.getTicketID());
            Developer dev = (Developer) appCenter.getUserByUsername(command.getUsername());
            if (!dev.getAssignedTickets().contains(ticket)) {
                throw new IllegalArgumentException("Ticket " + ticket.getId() + " is not assigned to developer " + dev.getUsername() + ".");
            }
            ticket.getHistory().add(new ActionBuilder()
                    .action("STATUS_CHANGED")
                    .oldStatus(ticket.getStatus().toString())
                    .by(command.getUsername())
                    .timestamp(command.getTimestamp())
                    .build());
            ticket.changeStatus();
            ticket.getHistory().getLast().setNewStatus(ticket.getStatus().toString());
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
