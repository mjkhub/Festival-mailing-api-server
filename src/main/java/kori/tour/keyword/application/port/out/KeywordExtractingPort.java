package kori.tour.keyword.application.port.out;

import java.util.List;

import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.domain.Tour;

public interface KeywordExtractingPort {
    /**
 * Persists the specified list of keywords associated with the given tour.
 *
 * @param tour the tour for which keywords are to be saved
 * @param keywordsOfTour the list of keywords to associate with the tour
 */
void saveKeyword(Tour tour, List<Keyword> keywordsOfTour);
}
