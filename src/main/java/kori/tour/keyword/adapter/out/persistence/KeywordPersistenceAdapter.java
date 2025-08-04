package kori.tour.keyword.adapter.out.persistence;

import java.util.List;

import kori.tour.common.annotation.PersistenceAdapter;
import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.adpater.out.persistence.TourJdbcRepository;
import kori.tour.tour.adpater.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class KeywordPersistenceAdapter implements KeywordExtractingPort {

    private final TourRepository tourRepository;
    private final KeywordRepository keywordRepository;
    private final TourJdbcRepository tourJdbcRepository;

    /**
     * Persists the given Tour entity and its associated list of Keyword entities.
     *
     * @param tour the Tour entity to be saved
     * @param keywordsOfTour the list of Keyword entities related to the Tour to be saved
     */
    @Override
    public void saveKeyword(Tour tour, List<Keyword> keywordsOfTour) {
        tourRepository.save(tour);
        keywordRepository.saveAll(keywordsOfTour);
    }
}
