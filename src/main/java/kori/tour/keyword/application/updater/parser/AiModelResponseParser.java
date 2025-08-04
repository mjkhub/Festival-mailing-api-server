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
