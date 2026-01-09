package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.List;

public class ViewAssignedTicketsCommand implements Command {
    public ViewAssignedTicketsCommand() {}

    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        ObjectNode commandNode =  mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        Developer dev = (Developer) appCenter.getUserByUsername(command.getUsername());
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
