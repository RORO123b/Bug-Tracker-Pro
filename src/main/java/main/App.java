package main;

import commands.*;
import users.UsersFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import users.User;
import commands.search.SearchCommand;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.UserInput;

import java.io.File;
import java.io.IOException;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public final class App {
    private App() {
    }

    private static final String INPUT_USERS_FIELD = "input/database/users.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER =
            MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath) throws IOException {
        AppCenter.resetInstance();

        // feel free to change this if needed
        // however keep 'outputs' variable name to be used for writing
        InputLoader inputLoader = new InputLoader(inputPath);
        InputLoader usersLoader = new InputLoader(INPUT_USERS_FIELD);
        ArrayNode outputs = MAPPER.createArrayNode();

        /*
            TODO 1 :
            Load initial user data and commands. we strongly recommend using jackson library.
            you can use the reading from hw1 as a reference.
            however you can use some of the more advanced features of
            jackson library, available here: https://www.baeldung.com/jackson-annotations
        */

        // TODO 2: process commands.

        // TODO 3: create objectnodes for output, add them to outputs list.\

        Invoker invoker = new Invoker();
        AppCenter appCenter = AppCenter.getInstance();

        for (UserInput userNode : usersLoader.getUsers()) {
            User user = UsersFactory.createByRole(
                    userNode.getRole(),
                    userNode.getUsername(),
                    userNode.getEmail(),
                    userNode.getHireDate(),
                    userNode.getExpertiseArea(),
                    userNode.getSeniority(),
                    userNode.getBusinessPriority(),
                    userNode.getSubordinates()
            );

            appCenter.getUsers().add(user);
        }

        int index = 0;
        for (CommandInput command : inputLoader.getCommands()) {
            if (index++ == 0) {
                appCenter.setDatePeriodStart(command.getTimestamp());
            }

            ObjectNode commandNode;
            switch (command.getCommand()) {
                case "reportTicket":
                    invoker.setCommand(new ReportTicketCommand());
                    break;
                case "viewTickets":
                    invoker.setCommand(new ViewTicketsCommand());
                    break;
                case "lostInvestors":
                    invoker.setCommand(new LostInvestorsCommand());
                    break;
                case "createMilestone":
                    invoker.setCommand(new CreateMilestoneCommand());
                    break;
                case "viewMilestones":
                    invoker.setCommand(new ViewMilestonesCommand());
                    break;
                case "assignTicket":
                    invoker.setCommand(new AssignTicketCommand());
                    break;
                case "viewAssignedTickets":
                    invoker.setCommand(new ViewAssignedTicketsCommand());
                    break;
                case "undoAssignTicket":
                    invoker.setCommand(new UndoAssignTicketCommand());
                    break;
                case "addComment":
                    invoker.setCommand(new AddCommentCommand());
                    break;
                case "undoAddComment":
                    invoker.setCommand(new UndoAddCommentCommand());
                    break;
                case "changeStatus":
                    invoker.setCommand(new ChangeStatusCommand());
                    break;
                case "undoChangeStatus":
                    invoker.setCommand(new UndoChangeStatusCommand());
                    break;
                case "viewTicketHistory":
                    invoker.setCommand(new ViewTicketHistoryCommand());
                    break;
                case "search":
                    invoker.setCommand(new SearchCommand());
                    break;
                case "viewNotifications":
                    invoker.setCommand(new ViewNotificationsCommand());
                    break;
                case "generateCustomerImpactReport":
                    invoker.setCommand(new GenerateCustomerImpactReportCommand());
                    break;
                case "generateTicketRiskReport":
                    invoker.setCommand(new GenerateTicketRiskReportCommand());
                    break;
                case "generateResolutionEfficiencyReport":
                    invoker.setCommand(new GenerateResolutionEfficiencyReportCommand());
                    break;
                case "appStabilityReport":
                    invoker.setCommand(new AppStabilityReportCommand());
                    break;
                case "generatePerformanceReport":
                    invoker.setCommand(new GeneratePerformanceReportCommand());
                    break;
                default:
                    invoker.setCommand(new LostInvestorsCommand());
                    break;
            }
            commandNode = invoker.pressButton(MAPPER, command);
            if (commandNode != null) {
                outputs.add(commandNode);
            }
        }
        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            WRITER.withDefaultPrettyPrinter().writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
