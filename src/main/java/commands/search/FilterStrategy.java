package commands.search;

import fileio.FilterInput;

public interface FilterStrategy<T> {
    /**
     * Checks if an item matches the given filters
     * @param item the item to check
     * @param filters the filters to apply
     * @return true if the item matches, false otherwise
     */
    boolean matches(T item, FilterInput filters);
}
