package kori.tour.tour.application.port.out;

import java.util.List;

import kori.tour.tour.application.updater.dto.NewTourDto;

public interface KeywordExtractingEventPort {

    void sendKeywordExtractingEvent(List<NewTourDto> newToursEntity);
}
