package commands;

import Milestones.Milestone;
import Users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public class CreateMilestoneCommand implements Command{
    public CreateMilestoneCommand() {}

    @Override
    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            for (Milestone existingMilestone : appCenter.getMilestones()) {
                if (existingMilestone.getTickets() != null) {
                    for (Tickets.Ticket ticket : existingMilestone.getTickets()) {
                        if (command.getTickets().contains(ticket.getId())) {
                            throw new IllegalArgumentException("Tickets " + ticket.getId() +
                                " already assigned to milestone " + existingMilestone.getName() + ".");
                        }
                    }
                }
            }

            User user = appCenter.getUserByUsername(command.getUsername());

            if (user.getRole() != "MANAGER")
                throw new IllegalArgumentException("The user does not have permission to execute this command: required role MANAGER; user role " + user.getRole() + ".");

            appCenter.addMilestone(new Milestone(
                command.getName(),
                command.getDueDate(),
                command.getBlockingFor(),
                command.getTickets(),
                command.getAssignedDevs(),
                command.getTimestamp(),
                command.getUsername()
            ));
            
            return null;
        } catch (IllegalArgumentException e) {
            ObjectNode error = mapper.createObjectNode();
            error.put("command", command.getCommand());
            error.put("username", command.getUsername());
            error.put("timestamp", command.getTimestamp().toString());
            error.put("error", e.getMessage());
            return error;
        }
    }
}
