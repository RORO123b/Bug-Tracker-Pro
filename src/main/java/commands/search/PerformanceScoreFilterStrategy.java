package commands.search;

import fileio.FilterInput;
import users.Developer;

public final class PerformanceScoreFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(final Developer dev, final FilterInput filters) {
        Double above = filters.getPerformanceScoreAbove();

        if (above != null && dev.getPerformanceScore() <= above) {
            return false;
        }

        return true;
    }
}
