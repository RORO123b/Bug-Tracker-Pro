package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CommandInput {
    private String command;
    private String username;
    private LocalDate timestamp;
    private String name;
    private LocalDate dueDate;
    private ArrayList<String> blockingFor;
    private ArrayList<Integer> tickets;
    private ArrayList<String> assignedDevs;
    Params params;
}
