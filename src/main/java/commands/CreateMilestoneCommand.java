package commands;

import milestones.Milestone;
import tickets.Ticket;
import tickets.action.ActionBuilder;
import users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public final class CreateMilestoneCommand implements Command {
    public CreateMilestoneCommand() { }

    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            for (Milestone existingMilestone : appCenter.getMilestones()) {
                for (Ticket ticket : existingMilestone.getTickets()) {
                    check(command, ticket, existingMilestone);
                }
            }

            User user = appCenter.getUserByUsername(command.getUsername());

            if (!user.getRole().equals("MANAGER")) {
                throw new IllegalArgumentException("The user does not have permission"
                        + " to execute this command: required role MANAGER; user role "
                        + user.getRole() + ".");
            }

            appCenter.addMilestone(new Milestone(
                    command.getName(),
                    command.getDueDate(),
                    command.getBlockingFor(),
                    command.getTickets(),
                    command.getAssignedDevs(),
                    command.getTimestamp(),
                    command.getUsername()
            ));
            for (Integer ticketId : command.getTickets()) {
                appCenter.getTicketById(ticketId).getHistory().add(new ActionBuilder()
                        .action("ADDED_TO_MILESTONE")
                        .milestone(command.getName())
                        .by(command.getUsername())
                        .timestamp(command.getTimestamp())
                        .build());
            }
            return null;
        } catch (IllegalArgumentException e) {
            ObjectNode error = CommandHelper.createErrorNode(mapper, command,
                    e.getMessage());
            return error;
        }
    }
    private void check(CommandInput command, Ticket ticket, Milestone existingMilestone) {
        if (command.getTickets().contains(ticket.getId())) {
            throw new IllegalArgumentException("Tickets "
                    + ticket.getId()
                    + " already assigned to milestone "
                    + existingMilestone.getName() + ".");
        }
    }
}
