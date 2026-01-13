package commands.search;

import fileio.FilterInput;
import users.Developer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperSearchEngine {
    private final List<FilterStrategy<Developer>> strategies;

    public DeveloperSearchEngine() {
        strategies = new ArrayList<>();
        strategies.add(new ExpertiseAreaFilterStrategy());
        strategies.add(new SeniorityFilterStrategy());
        strategies.add(new PerformanceScoreFilterStrategy());
    }

    public List<Developer> search(List<Developer> initialPool, FilterInput filters) {
        return initialPool.stream()
                .filter(dev -> strategies.stream().allMatch(s -> s.matches(dev, filters)))
                .sorted(Comparator.comparing(Developer::getUsername))
                .collect(Collectors.toList());
    }
}
