package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;

public final class Invoker {
    private Command slot;

    /**
     * @param command the command to set in the invoker slot
     */
    public void setCommand(final Command command) {
        this.slot = command;
    }

    /**
     * @param mapper the JSON object mapper
     * @param command the input data for the command
     * @return the result of the command execution as an ObjectNode
     */
    public ObjectNode pressButton(final ObjectMapper mapper, final CommandInput command) {
        AppCenter appCenter = AppCenter.getInstance();
        appCenter.updates(command.getTimestamp());
        return slot.execute(mapper, command);
    }
}
