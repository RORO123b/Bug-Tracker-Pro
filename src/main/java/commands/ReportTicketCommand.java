package commands;

import tickets.Ticket;
import tickets.BugBuilder;
import tickets.UIFeedbackBuilder;
import tickets.FeatureRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.Phases;
import enums.TicketType;
import fileio.CommandInput;
import main.AppCenter;

public final class ReportTicketCommand implements Command {
    public ReportTicketCommand() { }

    /**
     * @param mapper the object mapper for JSON creation
     * @param command the input data for reporting the ticket
     * @return an error node if an exception occurs, otherwise null
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        try {
            AppCenter appCenter = AppCenter.getInstance();
            if (appCenter.getUsers().stream().noneMatch(user -> user.getUsername()
                    .equals(command.getUsername()))) {
                throw new IllegalArgumentException("The user " + command.getUsername()
                        + " does not exist.");
            }
            if (appCenter.getCurrentPeriod() != Phases.TESTING) {
                throw new IllegalStateException("Tickets can only be reported "
                        + "during testing phases.");
            }
            if (command.getParams().getReportedBy().isEmpty()
                    && !command.getParams().getType().equals(TicketType.BUG)) {
                throw new IllegalArgumentException("Anonymous reports are only "
                        + "allowed for tickets of type BUG.");
            }
            Ticket ticket = null;
            if (command.getParams().getType().equals(TicketType.BUG)) {
                ticket = new BugBuilder()
                        .id(appCenter.getTickets().size())
                        .type(command.getParams().getType())
                        .title(command.getParams().getTitle())
                        .expertiseArea(command.getParams().getExpertiseArea())
                        .description(command.getParams().getDescription())
                        .reportedBy(command.getParams().getReportedBy())
                        .createdAt(command.getTimestamp().toString())
                        .businessPriority(command.getParams().getBusinessPriority())
                        .frequency(command.getParams().getFrequency())
                        .severity(command.getParams().getSeverity())
                        .environment(command.getParams().getEnvironment())
                        .errorCode(command.getParams().getErrorCode())
                        .build();
            } else if (command.getParams().getType().equals(TicketType.UI_FEEDBACK)) {
                ticket = new UIFeedbackBuilder()
                        .id(appCenter.getTickets().size())
                        .type(command.getParams().getType())
                        .title(command.getParams().getTitle())
                        .expertiseArea(command.getParams().getExpertiseArea())
                        .description(command.getParams().getDescription())
                        .reportedBy(command.getParams().getReportedBy())
                        .createdAt(command.getTimestamp().toString())
                        .businessPriority(command.getParams().getBusinessPriority())
                        .uiElementId(command.getParams().getUiElementId())
                        .businessValue(command.getParams().getBusinessValue())
                        .usabilityScore(command.getParams().getUsabilityScore())
                        .screenshotUrl(command.getParams().getScreenshotUrl())
                        .suggestedFix(command.getParams().getSuggestedFix())
                        .build();
            } else {
                ticket = new FeatureRequestBuilder()
                        .id(appCenter.getTickets().size())
                        .type(command.getParams().getType())
                        .title(command.getParams().getTitle())
                        .expertiseArea(command.getParams().getExpertiseArea())
                        .description(command.getParams().getDescription())
                        .reportedBy(command.getParams().getReportedBy())
                        .createdAt(command.getTimestamp().toString())
                        .businessPriority(command.getParams().getBusinessPriority())
                        .businessValue(command.getParams().getBusinessValue())
                        .customerDemand(command.getParams().getCustomerDemand())
                        .build();
            }
            appCenter.getTickets().add(ticket);
            return null;
        } catch (IllegalStateException | IllegalArgumentException e) {
            ObjectNode error = CommandHelper.createErrorNode(mapper, command,
                    e.getMessage());
            return error;
        }
    }
}
