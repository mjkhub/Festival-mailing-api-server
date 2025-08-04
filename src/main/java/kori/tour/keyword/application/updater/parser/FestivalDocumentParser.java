package kori.tour.keyword.application.updater.parser;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FestivalDocumentParser {

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Converts the given FestivalDocument to its JSON string representation.
	 *
	 * If serialization fails, returns the default string representation of this parser instance.
	 *
	 * @param festivalDocument the FestivalDocument to serialize
	 * @return the JSON string representation of the FestivalDocument, or this object's string representation if serialization fails
	 */
	public String mapToJson(FestivalDocument festivalDocument) {
		try {
			return objectMapper.writeValueAsString(festivalDocument);
		}
		catch (JsonProcessingException e) {
			return super.toString();
		}
	}

}
