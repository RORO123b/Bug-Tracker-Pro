package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class ViewTicketsCommand implements Command {
    public ViewTicketsCommand() {}

    public ObjectNode execute(ObjectMapper mapper, CommandInput command) {
        return null;
    }
}
