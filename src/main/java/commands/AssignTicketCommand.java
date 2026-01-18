package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.ExpertiseArea;
import enums.TicketStatus;
import fileio.CommandInput;
import main.AppCenter;
import milestones.Milestone;
import tickets.Ticket;
import tickets.action.ActionBuilder;
import users.Developer;

public final class AssignTicketCommand implements Command {
    public AssignTicketCommand() { }

    /**
     * @param mapper the object mapper
     * @param command the command input
     * @return the result node
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            Developer dev = (Developer) appCenter.getUserByUsername(command.getUsername());
            Ticket ticket = appCenter.getTicketById(command.getTicketID());

            if (ticket == null) {
                throw new IllegalArgumentException("Ticket " + command.getTicketID()
                        + " does not exist.");
            }

            if (ticket.getStatus() != TicketStatus.OPEN) {
                throw new IllegalStateException("Only OPEN tickets can be assigned.");
            }

            if (!appCenter.requiredExpertiseAreas(ticket).contains(dev.getExpertiseArea())) {
                String errorMessage = "Developer " + dev.getUsername()
                        + " cannot assign ticket " + command.getTicketID()
                        + " due to expertise area. Required: ";
                for (ExpertiseArea ea : appCenter.requiredExpertiseAreas(ticket)) {
                    errorMessage += ea.name() + ", ";
                }
                errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                errorMessage += "; Current: " + dev.getExpertiseArea() + ".";
                throw new IllegalArgumentException(errorMessage);
            }

            if (!appCenter.requiredSeniority(ticket).contains(dev.getSeniority())) {
                String errorMessage = "Developer " + dev.getUsername()
                        + " cannot assign ticket " + command.getTicketID()
                        + " due to seniority level. Required: ";
                for (String s : appCenter.requiredSeniority(ticket)) {
                    errorMessage += s + ", ";
                }
                errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                errorMessage += "; Current: " + dev.getSeniority() + ".";
                throw new IllegalArgumentException(errorMessage);
            }

            Milestone milestone = appCenter.getMilestoneByTicketID(ticket.getId());
            if (milestone == null
                    || !milestone.getAssignedDevs().contains(dev.getUsername())) {
                throw new IllegalArgumentException("Developer " + dev.getUsername()
                        + " is not assigned to milestone " + (milestone != null
                        ? milestone.getName() : "null") + ".");
            }

            if (milestone.getBlocked()) {
                throw new IllegalArgumentException("Cannot assign ticket " + ticket.getId()
                        + " from blocked milestone " + milestone.getName() + ".");
            }
            dev.addTicket(ticket);
            ticket.setAssignedAt(command.getTimestamp());
            ticket.setAssignedTo(dev.getUsername());
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticket.getHistory().add(new ActionBuilder()
                    .action("ASSIGNED")
                    .by(command.getUsername())
                    .timestamp(command.getTimestamp())
                    .build());
            ticket.getHistory().add(new ActionBuilder()
                    .action("STATUS_CHANGED")
                    .oldStatus(TicketStatus.OPEN.toString())
                    .newStatus(TicketStatus.IN_PROGRESS.toString())
                    .by(command.getUsername())
                    .timestamp(command.getTimestamp())
                    .build());
        } catch (IllegalStateException | IllegalArgumentException e) {
            ObjectNode error = CommandHelper.createErrorNode(mapper, command,
                    e.getMessage());
            return error;
        }

        return null;
    }
}
