package commands.search;

import fileio.FilterInput;
import users.User;

import java.util.List;

public interface SearchTypeStrategy {
    List<?> execute(User requester, FilterInput filters);
}
