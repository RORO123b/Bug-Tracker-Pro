package commands;

import Tickets.Ticket;
import Users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
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
        User user = appCenter.getUserByUsername(command.getUsername());
        ArrayNode ticketsArray = mapper.createArrayNode();

        if (user.getRole().equals("MANAGER")) {

            for (Ticket ticket : appCenter.getTickets()) {
                ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
                ticketsArray.add(ticketNode);
            }
        } else if (user.getRole().equals("DEVELOPER")) {
            for (Ticket ticket : appCenter.getTickets()) {
                if (ticket.getStatus().equals(TicketStatus.OPEN)) {
                    ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
                    ticketsArray.add(ticketNode);
                }
            }
        } else if (user.getRole().equals("REPORTER")) {
            for (Ticket ticket : appCenter.getTickets()) {
                if (ticket.getReportedBy().equals(command.getUsername())) {
                    ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
                    ticketsArray.add(ticketNode);
                }
            }
        }
        commandNode.set("tickets", ticketsArray);
        return commandNode;
    }
}
