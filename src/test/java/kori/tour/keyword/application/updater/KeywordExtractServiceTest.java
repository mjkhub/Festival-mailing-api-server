package kori.tour.keyword.application.updater;

import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.application.updater.parser.AiApiException;
import kori.tour.keyword.application.updater.parser.FestivalDocument;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest
class KeywordExtractServiceTest {

    @MockitoBean
    TourAiModelClient tourAiModelClient;

    @MockitoBean
    KeywordExtractingPort keywordExtractingPort;

    @Autowired
    KeywordExtractService keywordExtractService;

    @Test
    @DisplayName("키워드 추출: 정상 응답")
    void extractKeywords() throws AiApiException {
        //given
        FestivalDocument festivalDocument = getFestivalDocument();
        doReturn(getKeywordResponse()).when(tourAiModelClient).call(any());

        //when
        List<String> keywords = keywordExtractService.extractKeywords(festivalDocument);

        //then
        assertEquals(3, keywords.size());
        assertTrue(keywords.contains("keyword1"));
        assertTrue(keywords.contains("keyword2"));
        assertTrue(keywords.contains("keyword3"));
    }

    @Test
    @DisplayName("키워드 추출: 빈 Json")
    void extractKeywords_empty() throws AiApiException {
        //given
        FestivalDocument festivalDocument = getFestivalDocument();
        doReturn("{}").when(tourAiModelClient).call(any());

        //when
        List<String> keywords = keywordExtractService.extractKeywords(festivalDocument);

        //then
        assertTrue(keywords.isEmpty());
    }


    @Test
    @DisplayName("키워드 추출 실패 : 지정한 형식이 아닌 응답")
    void extractKeywords_invalidJson_throwsException() {
        // given
        FestivalDocument festivalDocument = getFestivalDocument();
        doReturn("wrong answer").when(tourAiModelClient).call(any());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> keywordExtractService.extractKeywords(festivalDocument);

        // then
        assertThatThrownBy(callable).isInstanceOf(AiApiException.class);
    }


    private String getKeywordResponse(){
        return  "{\"keywords\": [\"keyword1\", \"keyword2\", \"keyword3\"]}";
    }

    private FestivalDocument getFestivalDocument() {
        Map<String, String> festivalDetails = new HashMap<>();
        festivalDetails.put("행사일정", "2025년 4월 10일 ~ 4월 12일");
        festivalDetails.put("주최", "서울특별시");

        return FestivalDocument.builder()
                .festivalName("2025 봄꽃 페스티벌")
                .festivalDetails(festivalDetails)
                .build();
    }


}