package commands.search;

import enums.ExpertiseArea;
import fileio.FilterInput;
import users.Developer;

public final class ExpertiseAreaFilterStrategy implements FilterStrategy<Developer> {
    @Override
    public boolean matches(final Developer dev, final FilterInput filters) {
        ExpertiseArea filterExpertise = filters.getExpertiseArea();
        if (filterExpertise == null) {
            return true;
        }
        return dev.getExpertiseArea() == filterExpertise;
    }
}
