package kori.tour.tour.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TourResponse(String roadAddress, // addr1
		String basicAddress, // addr2
		String areaCode, String sigunGuCode, String contentId, String contentTypeId, LocalDate eventStartDate,
		LocalDate eventEndDate, String mainImageUrl, String mapX, String mapY, String mLevel,
		LocalDateTime modifiedTime, String telephone, String title) {
}
