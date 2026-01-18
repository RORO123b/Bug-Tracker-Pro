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
    private ObjectMapper mapper;
    
    public SearchCommand() { }

    /**
     * Executes the search command based on the specified search type
     * @param mapper the object mapper for JSON creation
     * @param command the input command details
     * @return the result node
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        this.mapper = mapper;

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        User requester = appCenter.getUserByUsername(command.getUsername());
        FilterInput filters = command.getFilters();

        commandNode.put("searchType", filters.getSearchType());

        SearchTypeStrategy strategy = getSearchStrategy(filters);
        List<?> results = strategy.execute(requester, filters);

        ArrayNode resultsArray = createResultsArray(results, filters, requester);
        commandNode.set("results", resultsArray);

        return commandNode;
    }

    private SearchTypeStrategy getSearchStrategy(final FilterInput filters) {
        if (filters.getSearchType().equals("DEVELOPER")) {
            return new DeveloperSearchTypeStrategy();
        }
        return new TicketSearchTypeStrategy();
    }

    private ArrayNode createResultsArray(final List<?> results, final FilterInput filters,
                                         final User requester) {
        ArrayNode resultsArray = mapper.createArrayNode();

        if (filters.getSearchType().equals("DEVELOPER")) {
            addDeveloperResults(resultsArray, results);
        } else {
            addTicketResults(resultsArray, results, requester);
        }

        return resultsArray;
    }

    private void addDeveloperResults(final ArrayNode resultsArray, final List<?> results) {
        for (Object obj : results) {
            Developer dev = (Developer) obj;
            ObjectNode devNode = createDeveloperNode(dev);
            resultsArray.add(devNode);
        }
    }

    private ObjectNode createDeveloperNode(final Developer dev) {
        ObjectNode devNode = mapper.createObjectNode();
        devNode.put("username", dev.getUsername());
        devNode.put("expertiseArea", dev.getExpertiseArea().toString());
        devNode.put("seniority", dev.getSeniority());
        devNode.put("performanceScore", dev.getPerformanceScore());
        devNode.put("hireDate", dev.getHireDate());
        return devNode;
    }

    private void addTicketResults(final ArrayNode resultsArray, final List<?> results,
                                  final User requester) {
        for (Object obj : results) {
            Ticket ticket = (Ticket) obj;
            ObjectNode ticketNode = createTicketNode(ticket, requester);
            resultsArray.add(ticketNode);
        }
    }

    private ObjectNode createTicketNode(final Ticket ticket, final User requester) {
        ObjectNode ticketNode = mapper.createObjectNode();
        ticketNode.put("id", ticket.getId());
        ticketNode.put("type", ticket.getType().toString());
        ticketNode.put("title", ticket.getTitle());
        ticketNode.put("businessPriority", ticket.getBusinessPriority().toString());
        ticketNode.put("status", ticket.getStatus().toString());
        ticketNode.put("createdAt", ticket.getCreatedAt());
        ticketNode.put("solvedAt", ticket.getSolvedAt() != null
                ? ticket.getSolvedAt().toString() : "");
        ticketNode.put("reportedBy", ticket.getReportedBy());

        if (requester.getRole().equals("MANAGER")) {
            ArrayNode matchingWordsArray = createMatchingWordsArray(ticket);
            ticketNode.set("matchingWords", matchingWordsArray);
        }

        return ticketNode;
    }

    private ArrayNode createMatchingWordsArray(final Ticket ticket) {
        ArrayNode matchingWordsArray = mapper.createArrayNode();

        if (ticket.getMatchingWords() == null) {
            return matchingWordsArray;
        }

        for (String word : ticket.getMatchingWords()) {
            matchingWordsArray.add(word);
        }

        return matchingWordsArray;
    }
}
