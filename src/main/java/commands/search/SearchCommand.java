package commands.search;

import commands.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.FilterInput;
import main.AppCenter;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.List;

public final class SearchCommand implements Command {
    public SearchCommand() { }

    /**
     * Executes the search command based on the specified search type
     * @param mapper the object mapper for JSON creation
     * @param command the input command details
     * @return the result node
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        User requester = appCenter.getUserByUsername(command.getUsername());
        FilterInput filters = command.getFilters();


        commandNode.put("searchType", filters.getSearchType());

        SearchTypeStrategy strategy;
        if (filters.getSearchType().equals("DEVELOPER")) {
            strategy = new DeveloperSearchTypeStrategy();
        } else {
            strategy = new TicketSearchTypeStrategy();
        }

        List<?> results = strategy.execute(requester, filters);
        ArrayNode resultsArray = mapper.createArrayNode();

        if (filters.getSearchType().equals("DEVELOPER")) {
            for (Object obj : results) {
                Developer dev = (Developer) obj;
                ObjectNode devNode = mapper.createObjectNode();
                devNode.put("username", dev.getUsername());
                devNode.put("expertiseArea", dev.getExpertiseArea() != null
                        ? dev.getExpertiseArea().toString() : "");
                devNode.put("seniority", dev.getSeniority() != null
                        ? dev.getSeniority() : "");
                devNode.put("performanceScore", dev.getPerformanceScore());
                devNode.put("hireDate", dev.getHireDate() != null
                        ? dev.getHireDate() : "");
                resultsArray.add(devNode);
            }
        } else {
            for (Object obj : results) {
                Ticket ticket = (Ticket) obj;
                ObjectNode ticketNode = mapper.createObjectNode();
                ticketNode.put("id", ticket.getId());
                ticketNode.put("type", ticket.getType() != null
                        ? ticket.getType().toString() : "");
                ticketNode.put("title", ticket.getTitle() != null
                        ? ticket.getTitle() : "");
                ticketNode.put("businessPriority", ticket.getBusinessPriority() != null
                        ? ticket.getBusinessPriority().toString() : "");
                ticketNode.put("status", ticket.getStatus() != null
                        ? ticket.getStatus().toString() : "");
                ticketNode.put("createdAt", ticket.getCreatedAt() != null
                        ? ticket.getCreatedAt() : "");
                ticketNode.put("solvedAt", ticket.getSolvedAt() != null
                        ? ticket.getSolvedAt().toString() : "");
                ticketNode.put("reportedBy", ticket.getReportedBy() != null
                        ? ticket.getReportedBy() : "");

                if (filters != null && filters.getKeywords() != null
                        && !filters.getKeywords().isEmpty()
                        && ticket.getMatchingWords() != null
                        && !ticket.getMatchingWords().isEmpty()) {
                    ArrayNode matchingWordsArray = mapper.createArrayNode();
                    for (String word : ticket.getMatchingWords()) {
                        matchingWordsArray.add(word);
                    }
                    ticketNode.set("matchingWords", matchingWordsArray);
                }

                resultsArray.add(ticketNode);
            }
        }

        commandNode.set("results", resultsArray);
        return commandNode;
    }
}
