package commands;

import milestones.Milestone;
import users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public final class ViewMilestonesCommand implements Command {
    public ViewMilestonesCommand() { }

    /**
     * Executes the view milestones command.
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());

        AppCenter appCenter = AppCenter.getInstance();
        ArrayNode milestonesArray = mapper.createArrayNode();
        User user = appCenter.getUserByUsername(command.getUsername());

        for (Milestone milestone : appCenter.getMilestones()) {
            if (user.getRole().equals("MANAGER") && milestone.getCreatedBy().equals(command.getUsername())
            || user.getRole().equals("DEVELOPER") && milestone.getAssignedDevs().contains(command.getUsername())) {
                ObjectNode milestoneNode = CommandHelper.createMilestoneNode(milestone, command);
                milestonesArray.add(milestoneNode);
            }
        }
        commandNode.set("milestones", milestonesArray);
        return commandNode;
    }
}
