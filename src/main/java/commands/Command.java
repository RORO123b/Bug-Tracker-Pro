package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public interface Command {
    /**
     * Executes command
     * @param mapper
     * @param command
     * @return ObjectNode used for output
     */
    ObjectNode execute(ObjectMapper mapper, CommandInput command);
}
