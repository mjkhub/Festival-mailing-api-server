package kori.tour.tour.domain;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum TourType {


	// 85는 한국 정보 15는 외외국어 정보
	FESTIVAL(List.of("85", "15")), RESTAURANT(List.of("82", "39"));

	private final List<String> contentTypeIdList;

	TourType(List<String> contentTypeIdList) {
		this.contentTypeIdList = contentTypeIdList;
	}

	public static TourType getTourType(String contentTypeId) {

		return Arrays.stream(TourType.values())
			.filter(tourType -> tourType.contentTypeIdList.contains(contentTypeId))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(" 잘못된 contentTypeId 입력" + contentTypeId));
	}

}
