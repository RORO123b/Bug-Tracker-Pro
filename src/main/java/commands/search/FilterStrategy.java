package commands.search;

import fileio.FilterInput;

public interface FilterStrategy<T> {
    boolean matches(T item, FilterInput filters);
}