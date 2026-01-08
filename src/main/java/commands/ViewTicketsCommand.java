package commands;

import Tickets.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public class ViewTicketsCommand implements Command {
    public ViewTicketsCommand() {}

    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());
        
        AppCenter appCenter = AppCenter.getInstance();
        ArrayNode ticketsArray = mapper.createArrayNode();
        
        if (appCenter.getTickets() != null) {
            for (Ticket ticket : appCenter.getTickets()) {
                ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
                ticketsArray.add(ticketNode);
            }
        }
        
        commandNode.set("tickets", ticketsArray);
        return commandNode;
    }
}
