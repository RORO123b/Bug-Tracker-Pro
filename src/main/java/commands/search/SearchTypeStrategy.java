package commands.search;

import fileio.FilterInput;
import users.User;

import java.util.List;

public interface SearchTypeStrategy {
    /**
     * Executes the search strategy
     * @param requester the user requesting the search
     * @param filters the filters to apply
     * @return a list of search results
     */
    List<?> execute(User requester, FilterInput filters);
}
