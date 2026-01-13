package commands.search;

import fileio.FilterInput;
import users.Developer;

public class PerformanceScoreFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(Developer dev, FilterInput filters) {
        Double above = filters.getPerformanceScoreAbove();

        if (above != null && dev.getPerformanceScore() <= above) {
            return false;
        }

        return true;
    }
}
