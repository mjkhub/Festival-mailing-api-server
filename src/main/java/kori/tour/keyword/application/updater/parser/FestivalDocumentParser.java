package kori.tour.keyword.application.updater.parser;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FestivalDocumentParser {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public String mapToJson(FestivalDocument festivalDocument) {
		try {
			return objectMapper.writeValueAsString(festivalDocument);
		}
		catch (JsonProcessingException e) {
			return super.toString();
		}
	}

}
