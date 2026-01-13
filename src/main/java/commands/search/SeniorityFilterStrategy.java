package commands.search;

import fileio.FilterInput;
import users.Developer;

public final class SeniorityFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(final Developer dev, final FilterInput filters) {
        String filterSeniority = filters.getSeniority();
        if (filterSeniority == null || filterSeniority.isEmpty()) {
            return true;
        }
        return dev.getSeniority().equals(filterSeniority);
    }
}
