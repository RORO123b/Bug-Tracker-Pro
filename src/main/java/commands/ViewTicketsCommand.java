package commands;

import tickets.Ticket;
import users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;

public final class ViewTicketsCommand implements Command {
    public ViewTicketsCommand() { }

    /**
     * Executes the view tickets command.
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        User user = appCenter.getUserByUsername(command.getUsername());
        ArrayNode ticketsArray = mapper.createArrayNode();

        if (user.getRole().equals("MANAGER")) {
            addManagerTickets(ticketsArray, appCenter);
        } else if (user.getRole().equals("DEVELOPER")) {
            addDeveloperTickets(ticketsArray, appCenter, user);
        } else if (user.getRole().equals("REPORTER")) {
            addReporterTickets(ticketsArray, appCenter, command.getUsername());
        }

        commandNode.set("tickets", ticketsArray);
        return commandNode;
    }

    private void addManagerTickets(final ArrayNode ticketsArray, final AppCenter appCenter) {
        for (Ticket ticket : appCenter.getTickets()) {
            ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
            ticketsArray.add(ticketNode);
        }
    }

    private void addDeveloperTickets(final ArrayNode ticketsArray, final AppCenter appCenter,
                                     final User user) {
        for (Ticket ticket : appCenter.getTickets()) {
            if (!ticket.getStatus().equals(TicketStatus.OPEN)) {
                continue;
            }

            if (!appCenter.getMilestoneByTicketID(ticket.getId())
                    .getAssignedDevs().contains(user.getUsername())) {
                continue;
            }

            ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
            ticketsArray.add(ticketNode);
        }
    }

    private void addReporterTickets(final ArrayNode ticketsArray, final AppCenter appCenter,
                                    final String username) {
        for (Ticket ticket : appCenter.getTickets()) {
            if (!ticket.getReportedBy().equals(username)) {
                continue;
            }

            ObjectNode ticketNode = CommandHelper.createTicketNode(ticket);
            ticketsArray.add(ticketNode);
        }
    }
}
