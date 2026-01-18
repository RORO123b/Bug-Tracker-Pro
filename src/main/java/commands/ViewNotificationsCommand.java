package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import main.AppCenter;
import users.Developer;

import java.util.ArrayList;

public class ViewNotificationsCommand implements Command {
    public ViewNotificationsCommand() { }

    @Override
    public final ObjectNode execute(final ObjectMapper mapper,
                                     final CommandInput command) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());
        commandNode.put("username", command.getUsername());
        commandNode.put("timestamp", command.getTimestamp().toString());
        ArrayNode notifications = mapper.createArrayNode();
        AppCenter appCenter = AppCenter.getInstance();
        Developer developer = (Developer) appCenter.getUserByUsername(command.getUsername());
        for (String notification : developer.getNotifications()) {
            notifications.add(notification);
        }
        developer.setNotifications(new ArrayList<>());
        commandNode.put("notifications", notifications);
        return commandNode;
    }
}
