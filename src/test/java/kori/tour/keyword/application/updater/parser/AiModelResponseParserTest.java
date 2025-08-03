package kori.tour.keyword.application.updater.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AiModelResponseParserTest {

    private final AiModelResponseParser parser = new AiModelResponseParser();

    @Test
    @DisplayName("AI 모델 파싱: 정상 케이스")
    void testMapToKeywords_WithValidJsonResponse() {
        //given
        String jsonResponse = "{\"keywords\": [\"keyword1\", \"keyword2\", \"keyword3\"], \"admissionFee\": \"무료\"}";

        //when
        List<String> parsedKeywords = parser.mapToKeywords(jsonResponse);

        //then
        assertEquals(3, parsedKeywords.size());
        assertTrue(parsedKeywords.contains("keyword1"));
        assertTrue(parsedKeywords.contains("keyword2"));
        assertTrue(parsedKeywords.contains("keyword3"));
    }

    @Test
    @DisplayName("AI 모델 파싱: 예외 케이스")
    void testMapToKeywords_WithEmptyJsonResponse() {
        //given
        String jsonResponse = "{}";

        //when
        List<String> parsedKeywords = parser.mapToKeywords(jsonResponse);

        //then
        assertNotNull(parsedKeywords);
        assertTrue(parsedKeywords.isEmpty());
    }


}
