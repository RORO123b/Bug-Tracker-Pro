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

        Set<String> found = new TreeSet<>();
        for (String kw : keywords) {
            String lowerKw = kw.toLowerCase();
            String matchingWord = findMatchingWord(title, lowerKw);
            if (matchingWord != null) {
                found.add(matchingWord);
            }
        }

        if (!found.isEmpty()) {
            ticket.setMatchingWords(new ArrayList<>(found));
            return true;
        }
        return false;
    }

    private String findMatchingWord(final String text, final String keyword) {
        if (!text.contains(keyword)) {
            return null;
        }
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.contains(keyword)) {
                return word;
            }
        }
        return keyword;
    }
}
