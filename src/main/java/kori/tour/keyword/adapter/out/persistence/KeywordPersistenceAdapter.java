package kori.tour.keyword.adapter.out.persistence;

import java.util.HashSet;
import java.util.List;

import kori.tour.global.annotation.PersistenceAdapter;
import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class KeywordPersistenceAdapter implements KeywordExtractingPort {

	private final TourRepository tourRepository;

	@Override
	public void saveKeyword(Tour tour, List<String> keywordsOfTour) {
		Tour savedTour = tourRepository.save(tour);
		savedTour.addKeywords(new HashSet<>(keywordsOfTour));
	}

}
