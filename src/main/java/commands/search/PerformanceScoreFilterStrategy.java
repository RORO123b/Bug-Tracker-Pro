package commands.search;

import fileio.FilterInput;
import users.Developer;

public final class PerformanceScoreFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(final Developer dev, final FilterInput filters) {
        Double above = filters.getPerformanceScoreAbove();

        if (above != null && dev.getPerformanceScore() < above) {
            return false;
        }

        Double below = filters.getPerformanceScoreBelow();

        if (below != null && dev.getPerformanceScore() > below) {
            return false;
        }

        return true;
    }
}
