package commands.search;

import fileio.FilterInput;
import users.Developer;

public class SeniorityFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(Developer dev, FilterInput filters) {
        String filterSeniority = filters.getSeniority();
        if (filterSeniority == null || filterSeniority.isEmpty()) {
            return true;
        }
        return dev.getSeniority().equals(filterSeniority);
    }
}
