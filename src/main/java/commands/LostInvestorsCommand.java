package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class LostInvestorsCommand implements Command {
    public LostInvestorsCommand() { }

    public ObjectNode execute(final ObjectMapper mapper, final CommandInput command) {
        return null;
    }
}
