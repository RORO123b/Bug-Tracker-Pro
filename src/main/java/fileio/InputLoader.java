package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class InputLoader {
    private ArrayList<CommandInput> commands;
    private ArrayList<UserInput> users;

    public InputLoader(final String filePath) throws IOException {
        if (filePath.contains("users")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            UserInput[] userArray = mapper.readValue(new File(filePath), UserInput[].class);
            this.users = new ArrayList<>(List.of(userArray));
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            CommandInput[] commandArray = mapper.readValue(new File(filePath),
                                                            CommandInput[].class);
            this.commands = new ArrayList<>(List.of(commandArray));
        }
    }
}
