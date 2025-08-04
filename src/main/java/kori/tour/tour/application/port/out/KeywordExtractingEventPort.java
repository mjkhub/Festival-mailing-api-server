package kori.tour.tour.application.port.out;

import java.util.List;

import kori.tour.tour.application.updater.dto.NewTourDto;

public interface KeywordExtractingEventPort {

    /**
 * Triggers a keyword extraction event for the provided list of new tour data.
 *
 * @param newToursEntity the list of new tour data transfer objects to process for keyword extraction
 */
void sendKeywordExtractingEvent(List<NewTourDto> newToursEntity);
}
