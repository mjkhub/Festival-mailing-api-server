package kori.tour.keyword.application.updater;

import java.util.List;

import kori.tour.tour.application.updater.dto.NewTourDto;

public record KeywordExtractingEvent(List<NewTourDto> newToursEntity, String eventId) {

}
