package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.Phases;
import fileio.CommandInput;
import main.AppCenter;
import milestones.Milestone;
import enums.TicketStatus;
import tickets.Ticket;

public final class StartTestingPhaseCommand implements Command {
    public StartTestingPhaseCommand() { }

    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();

            for (Milestone milestone : appCenter.getMilestones()) {
                for (Ticket ticket : milestone.getTickets()) {
                    if (ticket.getStatus() != TicketStatus.CLOSED) {
                        throw new IllegalStateException(
                                "Cannot start a new testing phase.");
                    }
                }
            }

            appCenter.setCurrentPeriod(Phases.TESTING);
            appCenter.setDatePeriodStart(command.getTimestamp());

            return null;
        } catch (IllegalStateException e) {
            ObjectNode error = CommandHelper.createErrorNode(mapper, command,
                    e.getMessage());
            return error;
        }
    }
}
