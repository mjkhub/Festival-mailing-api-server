package kori.tour.tour.adapter.out.event;

import java.util.List;
import java.util.UUID;

import kori.tour.common.annotation.EventAdapter;
import kori.tour.common.events.Events;
import kori.tour.keyword.application.updater.KeywordExtractingEvent;
import kori.tour.tour.application.port.out.KeywordExtractingEventPort;
import kori.tour.tour.application.updater.dto.NewTourDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@EventAdapter
@RequiredArgsConstructor
class KeywordExtractingEventAdapter implements KeywordExtractingEventPort {

    @Override
    public void sendKeywordExtractingEvent(List<NewTourDto> newToursEntity) {
        String uuid = UUID.randomUUID().toString();
        log.info("KeywordExtractingEvent sent eventId = {}", uuid);
        Events.raise(new KeywordExtractingEvent(newToursEntity, uuid));
    }
}
