package kori.tour.keyword.application.updater;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import kori.tour.common.events.Events;


@SpringBootTest
class KeywordExtractEventListenerTest {


    @Test
    void test1234() {
        Events.raise(new KeywordExtractingEvent(null, UUID.randomUUID().toString()));
    }

}
