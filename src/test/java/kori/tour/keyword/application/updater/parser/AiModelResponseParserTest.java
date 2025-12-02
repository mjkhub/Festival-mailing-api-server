package kori.tour.keyword.application.updater.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import kori.tour.global.exception.code.ErrorCode;

class AiModelResponseParserTest {

	private final AiModelResponseParser parser = new AiModelResponseParser();

	@Test
	@DisplayName("키워드 추출: 정상 응답")
	void givenKeywordsArray_whenMapToKeywords_thenReturnsSameList() {
		String json = """
			{
			  "keywords": ["mountain", "lake", "festival"]
			}
			""";

		// when
		List<String> keywords = parser.mapToKeywords(json);

		// then
		assertThat(keywords).containsExactly("mountain", "lake", "festival");
	}

	@Test
	@DisplayName("키워드 추출: 잘못된 JSON")
	void givenMalformedJson_whenMapToKeywords_thenThrowsAiApiException() {
		// when
		AiApiException exception = assertThrows(AiApiException.class, () -> parser.mapToKeywords("{"));

		// then
		assertThat(exception).hasMessage(ErrorCode.AI_RESPONSE_WRONG_FORMAT.getMessage());
		assertThat(exception.getCause()).isInstanceOf(JsonProcessingException.class);
	}

}
