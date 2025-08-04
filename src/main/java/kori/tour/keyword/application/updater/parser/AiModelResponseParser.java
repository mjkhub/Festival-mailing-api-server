package kori.tour.keyword.application.updater.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AiModelResponseParser {

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Extracts a list of keywords from the "keywords" array field in a JSON response string.
	 *
	 * If the "keywords" field is present and is an array, each element is converted to a string and added to the result list.
	 * Returns an empty list if parsing fails or if the "keywords" field is absent or not an array.
	 *
	 * @param jsonResponse the JSON string containing the AI model response
	 * @return a list of keyword strings extracted from the response; empty if extraction fails
	 */
	public List<String> mapToKeywords(String jsonResponse) {
		List<String> keywords = new ArrayList<>();

		try {
			JsonNode root = mapper.readTree(jsonResponse);
			JsonNode keywordsNode = root.path("keywords");

			if (keywordsNode.isArray()) {
				for (JsonNode node : keywordsNode) {
					keywords.add(node.asText());
				}
			}
		} catch (JsonProcessingException e) {
			log.warn("AI 키워드 응답 파싱중 에러 발생 response = {}", jsonResponse, e);
		}

		return keywords;
	}

}
