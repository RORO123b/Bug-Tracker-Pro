package commands;

import java.util.List;

import Milestones.Milestone;
import Users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public class ViewMilestonesCommand implements Command {
    public ViewMilestonesCommand() {}

    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());
        
        AppCenter appCenter = AppCenter.getInstance();
        ArrayNode milstonesArray = mapper.createArrayNode();
        User user = appCenter.getUserByUsername(command.getUsername());

        if (user.getRole().equals("MANAGER")) {
            for (Milestone milestone : appCenter.getMilestones()) {
                if (milestone.getCreatedBy().equals(command.getUsername())) {
                    ObjectNode milestoneNode = CommandHelper.createMilestoneNode(milestone, command);
                    milstonesArray.add(milestoneNode);
                }
            }
        } else if (user.getRole().equals("DEVELOPER")) {
            for (Milestone milestone : appCenter.getMilestones()) {
                if (milestone.getAssignedDevs().contains(command.getUsername())) {
                    ObjectNode milestoneNode = CommandHelper.createMilestoneNode(milestone, command);
                    milstonesArray.add(milestoneNode);
                }
            }
        }
        commandNode.set("milestones", milstonesArray);
        return commandNode;
    }
}
