package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public final class LostInvestorsCommand implements Command {
    public LostInvestorsCommand() { }

    /**
     * Executes the lost investors command
     * @param mapper the object mapper
     * @param command the command input
     * @return the result as an ObjectNode
     */
    @Override
    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        return null;
    }
}
