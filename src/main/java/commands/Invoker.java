package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.App;
import main.AppCenter;

import java.util.ArrayDeque;
import java.util.Deque;

public class Invoker {
    private Command slot;

    public void setCommand(Command command) {
        this.slot = command;
    }

    public ObjectNode pressButton(ObjectMapper mapper, CommandInput command) {
        AppCenter appCenter = AppCenter.getInstance();
        appCenter.updates(command.getTimestamp());
        return slot.execute(mapper, command);
    }
}
