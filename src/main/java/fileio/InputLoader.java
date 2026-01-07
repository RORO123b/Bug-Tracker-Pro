package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic skeleton for loading input JSON file as a Map.
 * Students should implement deeper parsing themselves.
 */
@Getter
public class InputLoader {
    private final ArrayList<CommandInput> commands;

    public InputLoader(final String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Read the array directly instead of expecting an object wrapper
        CommandInput[] commandArray = mapper.readValue(new File(filePath), CommandInput[].class);
        this.commands = new ArrayList<>(List.of(commandArray));
    }
}
