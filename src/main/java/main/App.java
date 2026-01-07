package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Invoker;
import commands.ReportTicketCommand;
import commands.ViewTicketsCommand;
import fileio.CommandInput;
import fileio.InputLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public class App {
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
        // feel free to change this if needed
        // however keep 'outputs' variable name to be used for writing
        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode outputs = MAPPER.createArrayNode();

        /*
            TODO 1 :
            Load initial user data and commands. we strongly recommend using jackson library.
            you can use the reading from hw1 as a reference.
            however you can use some of the more advanced features of
            jackson library, available here: https://www.baeldung.com/jackson-annotations
        */

        // TODO 2: process commands.

        // TODO 3: create objectnodes for output, add them to outputs list.
        Invoker invoker = new Invoker();

        for (CommandInput command : inputLoader.getCommands()) {
            ObjectNode commandNode;

            switch (command.getCommand()) {
                case "reportTicket":
                    invoker.setCommand(new ReportTicketCommand());
                    commandNode = invoker.pressButton(MAPPER, command);
                    break;
                case "viewTickets":
                    invoker.setCommand(new ViewTicketsCommand());
                    commandNode = invoker.pressButton(MAPPER, command);
                default:
                    commandNode = MAPPER.createObjectNode();
                    commandNode.put("command", command.getCommand());
                    break;
            }
            outputs.add(commandNode);
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
