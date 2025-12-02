package kori.tour.keyword.application.updater;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kori.tour.global.exception.code.ErrorCode;
import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.application.updater.parser.AiApiException;
import kori.tour.keyword.application.updater.parser.AiModelResponseParser;
import kori.tour.keyword.application.updater.parser.FestivalDocument;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KeywordExtractServiceTest.TestConfig.class)
class KeywordExtractServiceTest {

	@Autowired
	private KeywordExtractService keywordExtractService;

	@Autowired
	private TourKeywordAiModelClient tourKeywordAiModelClient;

	@Autowired
	private AiModelResponseParser aiModelResponseParser;

	@Autowired
	private PromptBuilder promptBuilder;

	@Autowired
	private KeywordEvaluator keywordEvaluator;

	@Autowired
	private KeywordExtractingPort keywordExtractingPort;

	private FestivalDocument festivalDocument;

	@BeforeEach
	void setUp() {
		Mockito.reset(tourKeywordAiModelClient, aiModelResponseParser, promptBuilder, keywordEvaluator,
			keywordExtractingPort);

		festivalDocument = FestivalDocument.builder()
			.festivalName("2025 봄꽃 페스티벌")
			.festivalDetails(Map.of("행사일정", "2025년 4월 10일 ~ 4월 12일"))
			.build();
	}

	@Test
	@DisplayName("키워드 추출 서비스: 정상 응답")
	void givenValidAiResponse_whenExtractKeywords_thenReturnKeywords() {
		// given
		Prompt prompt = new Prompt("문서에서 키워드를 추출");
		String aiResponse = "{\"keywords\": [\"keyword1\", \"keyword2\"]}";
		List<String> parsedKeywords = List.of("keyword1", "keyword2");
		given(promptBuilder.buildKeywordPrompt(anyString())).willReturn(prompt);
		given(tourKeywordAiModelClient.call(prompt)).willReturn(aiResponse);
		given(aiModelResponseParser.mapToKeywords(aiResponse)).willReturn(parsedKeywords);
		given(keywordEvaluator.evaluate(any(EvaluationRequest.class))).willReturn(new EvaluationResponse(true, 0, null, null));

		// when
		List<String> result = keywordExtractService.extractKeywords(festivalDocument);

		// then
		assertThat(result).containsExactlyElementsOf(parsedKeywords);
		then(tourKeywordAiModelClient).should().call(prompt);
		then(keywordEvaluator).should().evaluate(any(EvaluationRequest.class));
	}

	@Test
	@DisplayName("키워드 추출 서비스: 키워드 평가 실패 시 모든 재시도 후, 빈 키워드 리스트 반환")
	void givenEvaluationAlwaysFails_whenExtractKeywords_thenReturnEmptyList() {
		// given
		Prompt prompt = new Prompt("문서에서 키워드를 추출");
		String aiResponse = "{\"keywords\": [\"keyword1\"]}";
		List<String> parsedKeywords = List.of("keyword1");
		given(promptBuilder.buildKeywordPrompt(anyString())).willReturn(prompt);
		given(tourKeywordAiModelClient.call(prompt)).willReturn(aiResponse);
		given(aiModelResponseParser.mapToKeywords(aiResponse)).willReturn(parsedKeywords);
		given(keywordEvaluator.evaluate(any(EvaluationRequest.class))).willReturn(new EvaluationResponse(false, 0, null, null));

		// when
		List<String> result = keywordExtractService.extractKeywords(festivalDocument);

		// then
		assertThat(result).isEmpty();
		then(keywordEvaluator).should(times(3)).evaluate(any(EvaluationRequest.class));
		then(tourKeywordAiModelClient).should(times(3)).call(prompt);
	}

	@Test
	@DisplayName("키워드 추출 서비스: AiApiException 발생 -> 재시도 후 성공")
	void givenParserThrowsTwice_whenExtractKeywords_thenRetryAndSucceed() {
		// given
		Prompt prompt = new Prompt("문서에서 키워드를 추출");
		String aiResponse = "{\"keywords\": [\"keyword1\", \"keyword2\"]}";
		List<String> parsedKeywords = List.of("keyword1", "keyword2");
		given(promptBuilder.buildKeywordPrompt(anyString())).willReturn(prompt);
		given(tourKeywordAiModelClient.call(prompt)).willReturn(aiResponse);
		given(aiModelResponseParser.mapToKeywords(aiResponse))
			.willThrow(new AiApiException(ErrorCode.AI_RESPONSE_WRONG_FORMAT))
			.willThrow(new AiApiException(ErrorCode.AI_RESPONSE_WRONG_FORMAT))
			.willReturn(parsedKeywords);
		given(keywordEvaluator.evaluate(any(EvaluationRequest.class))).willReturn(new EvaluationResponse(true, 0, null, null));

		// when
		List<String> result = keywordExtractService.extractKeywords(festivalDocument);

		// then
		assertThat(result).containsExactlyElementsOf(parsedKeywords);
		then(tourKeywordAiModelClient).should(times(3)).call(prompt);
		then(aiModelResponseParser).should(times(3)).mapToKeywords(aiResponse);
	}

	@Test
	@DisplayName("키워드 추출 서비스: 최대 재시도 실패 시 복구 로직 실행")
	void givenParserAlwaysFails_whenExtractKeywords_thenRecoverWithEmptyList() {
		// given
		Prompt prompt = new Prompt("문서에서 키워드를 추출");
		String aiResponse = "{\"keywords\": []}";
		given(promptBuilder.buildKeywordPrompt(anyString())).willReturn(prompt);
		given(tourKeywordAiModelClient.call(prompt)).willReturn(aiResponse);
		given(aiModelResponseParser.mapToKeywords(aiResponse)).willThrow(new AiApiException(ErrorCode.AI_RESPONSE_WRONG_FORMAT));

		// when
		List<String> result = keywordExtractService.extractKeywords(festivalDocument);

		// then
		assertThat(result).isEmpty();
		then(aiModelResponseParser).should(times(3)).mapToKeywords(aiResponse);
	}

	@TestConfiguration
	@EnableRetry
	static class TestConfig {

		@Bean
		KeywordExtractService keywordExtractService(
			TourKeywordAiModelClient tourKeywordAiModelClient,
			AiModelResponseParser aiModelResponseParser,
			PromptBuilder promptBuilder,
			KeywordExtractingPort keywordExtractingPort,
			KeywordEvaluator keywordEvaluator
		) {
			return new KeywordExtractService(tourKeywordAiModelClient, aiModelResponseParser, promptBuilder,
				keywordExtractingPort, keywordEvaluator);
		}

		@Bean
		TourKeywordAiModelClient tourKeywordAiModelClient() {
			return Mockito.mock(TourKeywordAiModelClient.class);
		}

		@Bean
		AiModelResponseParser aiModelResponseParser() {
			return Mockito.mock(AiModelResponseParser.class);
		}

		@Bean
		PromptBuilder promptBuilder() {
			return Mockito.mock(PromptBuilder.class);
		}

		@Bean
		KeywordEvaluator keywordEvaluator() {
			return Mockito.mock(KeywordEvaluator.class);
		}

		@Bean
		KeywordExtractingPort keywordExtractingPort() {
			return Mockito.mock(KeywordExtractingPort.class);
		}

		@Bean
		Sleeper noDelaySleeper() { // @EnableRetry 어노테이션 내부 Sleeper에 주입되는 빈
			return backOffPeriod -> { }; // delay 0 for test
		}
	}

}
