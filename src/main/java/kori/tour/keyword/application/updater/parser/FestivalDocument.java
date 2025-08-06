package kori.tour.keyword.application.updater.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourRepeat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FestivalDocument {

	private String festivalName;

	private Map<String, String> festivalDetails;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static FestivalDocument createFestivalDocument(NewTourDto newTourDto) {

		return FestivalDocument.builder()
				.festivalName(newTourDto.getTour().getTitle())
				.festivalDetails(newTourDto.getTourRepeatList().stream()
								.collect(Collectors.toMap(TourRepeat::getInfoName, TourRepeat::getInfoText)))
				.build();
	}

	/**
	 * Util 로 빼는걸 고려했지만, 객체를 Json String 으로 직접 변환하는 기능은 여기서만 필요해서 클래스 내부에
	 * */
	public static String toJson(FestivalDocument festivalDocument) {
		try {
			return objectMapper.writeValueAsString(festivalDocument);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON serialization failed", e);
		}
	}

}
