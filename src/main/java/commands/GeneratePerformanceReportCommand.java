package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import users.Developer;
import users.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratePerformanceReportCommand implements Command {
    public GeneratePerformanceReportCommand() { }

    @Override
    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        AppCenter appCenter = AppCenter.getInstance();
        Manager manager = (Manager) appCenter.getUserByUsername(command.getUsername());
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        ArrayNode reportNode = mapper.createArrayNode();
        List<String> sortedDevs = new ArrayList<>(manager.getSubordinates());
        Collections.sort(sortedDevs);
        for (String developer : sortedDevs) {
            Developer dev = (Developer) appCenter.getUserByUsername(developer);
            ObjectNode developerNode = mapper.createObjectNode();
            developerNode.put("username", developer);
            developerNode.put("closedTickets", dev.getClosedTicketsLastMonth(command.getTimestamp().minusMonths(1)).size());
            developerNode.put("averageResolutionTime", Math.round(dev.getAverageResolutionTime(command.getTimestamp().minusMonths(1)) * 100.0) / 100.0);
            developerNode.put("performanceScore", Math.round(dev.getPerformanceScore(command.getTimestamp()) * 100.0) / 100.0);
            developerNode.put("seniority", dev.getSeniority());
            reportNode.add(developerNode);
        }
        commandNode.put("report", reportNode);
        return commandNode;
    }
}
