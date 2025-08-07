package kori.tour.keyword.application.port.out;

import java.util.List;

import kori.tour.tour.domain.Tour;

public interface KeywordExtractingPort {

	void saveKeyword(Tour tour, List<String> keywordsOfTour);

}
