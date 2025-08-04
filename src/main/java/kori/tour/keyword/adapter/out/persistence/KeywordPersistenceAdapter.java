package kori.tour.keyword.adapter.out.persistence;

import java.util.List;

import kori.tour.common.annotation.PersistenceAdapter;
import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.adapter.out.persistence.TourJdbcRepository;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class KeywordPersistenceAdapter implements KeywordExtractingPort {

    private final TourRepository tourRepository;
    private final KeywordRepository keywordRepository;
    private final TourJdbcRepository tourJdbcRepository;

    @Override
    public void saveKeyword(Tour tour, List<Keyword> keywordsOfTour) {
        tourRepository.save(tour);
        keywordRepository.saveAll(keywordsOfTour);
    }
}
