package kori.tour.global.data.area_code;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AreaCodeParser {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<Area> parseToAreaCode(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<Area>>() {
			});
		}
		catch (Exception e) {
			throw new RuntimeException("지역 코드 JSON 파싱 실패", e);
		}
	}

}
