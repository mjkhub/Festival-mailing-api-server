package kori.tour.keyword.application.updater;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import kori.tour.common.events.Events;
import kori.tour.email.application.updater.EmailSendEvent;
import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.application.updater.dto.NewTourDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordExtractEventListener {

    private final KeywordExtractService keywordExtractService;
    private final ThreadPoolTaskExecutor tourUpdaterThreadTaskExecutor;

    // 키워드는 많아봐야 네개라서, 모아서 처리하는 것보다, 이어서 바로 저장하는게 나을 것 같음.
    // 어차피 AI 에게 요청을 Tour 단위로 하는 상황이니까 스레드를 빠르게 돌리는게 나을듯?
    // 이것도 어딘가에 기록해두자

    @EventListener
    public void listenKeywordExtractEvent(KeywordExtractingEvent event) {
        log.info("KeywordExtractingEvent={} received ", event.eventId());
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (NewTourDto newTourDto : event.newToursEntity()) {
            CompletableFuture<Void> future = CompletableFuture
                    .supplyAsync(() -> keywordExtractService.extractKeywords(newTourDto), tourUpdaterThreadTaskExecutor)
                    .thenApply(keywords -> keywords.stream()
                            .map(keyword -> Keyword.builder()
                                    .keyword(keyword)
                                    .tour(newTourDto.getTour())
                                    .build())
                            .toList())
                    .thenApply(keywordEntities -> keywordExtractService.saveKeywords(newTourDto, keywordEntities))
                    .thenAccept( entry -> Events.raise(new EmailSendEvent(entry)));
            futures.add(future);
        }
        long endTime = System.currentTimeMillis();
        log.info("KeywordExtractingEvent={} saved {} 개 Tour의 keyword 저장 Total time: {}ms", event.eventId(), event.newToursEntity().size(), (endTime - startTime));

    }

}
