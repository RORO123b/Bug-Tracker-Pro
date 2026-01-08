package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public interface Command {
    public ObjectNode execute(ObjectMapper mapper, CommandInput command);
}
