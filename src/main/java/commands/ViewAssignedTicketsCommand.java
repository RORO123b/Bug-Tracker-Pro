package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.List;

public final class ViewAssignedTicketsCommand implements Command {
    public ViewAssignedTicketsCommand() { }

    /**
     * Executes the view assigned tickets command
     * @param mapper the object mapper for JSON creation
     * @param command the input command details
     * @return the result node
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        ObjectNode commandNode =  mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        User user = appCenter.getUserByUsername(command.getUsername());

        if (!(user.getRole().equals("DEVELOPER"))) {
            commandNode.put("error", "The user does not have permission to execute this command: "
                    + "required role DEVELOPER; user role " + user.getRole() + ".");
            return commandNode;
        }

        Developer dev = (Developer) user;
        ArrayNode ticketsArray = mapper.createArrayNode();
        List<Ticket> assignedTickets = dev.getAssignedTickets();

        for (Ticket ticket : assignedTickets) {
            ObjectNode ticketNode = CommandHelper.createAssignedTicketNode(ticket);
            ticketsArray.add(ticketNode);
        }

        commandNode.set("assignedTickets", ticketsArray);
        return commandNode;
    }
}
