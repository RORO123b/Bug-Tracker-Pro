package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.BusinessPriority;
import enums.TicketType;
import fileio.CommandInput;
import main.AppCenter;
import tickets.Ticket;

import java.util.List;

public class GenerateCustomerImpactReportCommand implements Command {
    public GenerateCustomerImpactReportCommand() { }

    @Override
    public final ObjectNode execute(final ObjectMapper mapper,
                                     final CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        ObjectNode reportNode = mapper.createObjectNode();
        AppCenter appCenter = AppCenter.getInstance();
        List<Ticket> tickets = appCenter.getOpenInProgressTickets();
        reportNode.put("totalTickets", tickets.size());

        ObjectNode ticketsByTypeNode = mapper.createObjectNode();
        ticketsByTypeNode.put("BUG",
                appCenter.getTicketsByType(TicketType.BUG, tickets).size());
        ticketsByTypeNode.put("FEATURE_REQUEST",
                appCenter.getTicketsByType(
                        TicketType.FEATURE_REQUEST, tickets).size());
        ticketsByTypeNode.put("UI_FEEDBACK",
                appCenter.getTicketsByType(
                        TicketType.UI_FEEDBACK, tickets).size());

        reportNode.put("ticketsByType", ticketsByTypeNode);

        ObjectNode ticketsByPriority = mapper.createObjectNode();
        ticketsByPriority.put("LOW",
                appCenter.getTicketsByPriority(
                        BusinessPriority.LOW, tickets).size());
        ticketsByPriority.put("MEDIUM",
                appCenter.getTicketsByPriority(
                        BusinessPriority.MEDIUM, tickets).size());
        ticketsByPriority.put("HIGH",
                appCenter.getTicketsByPriority(
                        BusinessPriority.HIGH, tickets).size());
        ticketsByPriority.put("CRITICAL",
                appCenter.getTicketsByPriority(
                        BusinessPriority.CRITICAL, tickets).size());

        reportNode.put("ticketsByPriority", ticketsByPriority);

        ObjectNode customerImpactByType = mapper.createObjectNode();
        customerImpactByType.put("BUG",
                appCenter.calculateAverageImpact(
                        appCenter.getTicketsByType(TicketType.BUG, tickets)));
        customerImpactByType.put("FEATURE_REQUEST",
                appCenter.calculateAverageImpact(
                        appCenter.getTicketsByType(
                                TicketType.FEATURE_REQUEST, tickets)));
        customerImpactByType.put("UI_FEEDBACK",
                appCenter.calculateAverageImpact(
                        appCenter.getTicketsByType(
                                TicketType.UI_FEEDBACK, tickets)));

        reportNode.put("customerImpactByType", customerImpactByType);

        commandNode.put("report", reportNode);
        return commandNode;
    }
}
