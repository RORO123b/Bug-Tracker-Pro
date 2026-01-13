package commands.search;

import fileio.FilterInput;
import tickets.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class KeywordFilterStrategy implements FilterStrategy<Ticket> {
    @Override
    public boolean matches(final Ticket ticket, final FilterInput filters) {
        List<String> keywords = filters.getKeywords();
        if (keywords == null || keywords.isEmpty()) {
            return true;
        }

        String title = ticket.getTitle().toLowerCase();
        String description = (ticket.getDescription() != null)
                ? ticket.getDescription().toLowerCase() : "";

        Set<String> found = new TreeSet<>();
        for (String kw : keywords) {
            String lowerKw = kw.toLowerCase();
            if (title.contains(lowerKw) || description.contains(lowerKw)) {
                found.add(lowerKw);
            }
        }

        if (!found.isEmpty()) {
            ticket.setMatchingWords(new ArrayList<>(found));
            return true;
        }
        return false;
    }
}
