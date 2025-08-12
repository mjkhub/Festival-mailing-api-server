package kori.tour.keyword.application.updater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.application.updater.parser.AiApiException;
import kori.tour.keyword.application.updater.parser.AiModelResponseParser;
import kori.tour.keyword.application.updater.parser.FestivalDocument;
import kori.tour.tour.application.updater.dto.NewTourDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordExtractService {

	private final TourKeywordAiModelClient tourKeywordAiModelClient;

	private final AiModelResponseParser aiModelResponseParser;

	private final PromptBuilder promptBuilder;

	private final KeywordExtractingPort keywordExtractingPort;

	private final KeywordEvaluator keywordEvaluator;

	private static final int RETRY_PERIOD = 1000;

	@Retryable(retryFor = { AiApiException.class }, backoff = @Backoff(delay = RETRY_PERIOD), maxAttempts = 3)
	public List<String> extractKeywords(FestivalDocument festivalDocument) {
		String jsonDocument = FestivalDocument.toJson(festivalDocument);
		Prompt prompt = promptBuilder.buildKeywordPrompt(jsonDocument);
		String aiResponse = tourKeywordAiModelClient.call(prompt);
		List<String> keywords = aiModelResponseParser.mapToKeywords(aiResponse);
		EvaluationResponse evaluationResponse = keywordEvaluator.evaluate(new EvaluationRequest(null, null, keywords.toString()));
		if(!evaluationResponse.isPass()) throw new RuntimeException();
		return keywords;
	}

	@Transactional
	public Map.Entry<NewTourDto, List<String>> saveKeywords(NewTourDto newTourDto, List<String> keywordsOfTour) {
		keywordExtractingPort.saveKeyword(newTourDto.getTour(), keywordsOfTour);
		return Map.entry(newTourDto, keywordsOfTour);
	}

	@Recover
	public List<String> recoverFromAiFailure(AiApiException e, FestivalDocument festivalDocument) {
		log.error("AI 키워드 추출 실패 - 모든 재시도 시도 후에도 응답 없음. festivalDocument={}, message={}",
				FestivalDocument.toJson(festivalDocument), e.getMessage(), e);
		return new ArrayList<>(); // 일단 정상흐름으로
	}

}
