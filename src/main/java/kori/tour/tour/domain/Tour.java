package kori.tour.tour.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import kori.tour.tour.domain.dto.TourResponse;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Tour {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tour_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Language language;

	private String roadAddress; // addr1

	private String basicAddress; // addr2

	private String areaCode;

	private String sigunGuCode;

	@Column(unique = true)
	private String contentId;

	@Enumerated(EnumType.STRING)
	private TourType contentTypeId;

	private LocalDate eventStartDate;

	private LocalDate eventEndDate;

	private String mainImageUrl;

	private String mapX; // 좌표와 관련된 필드는 분리하고 싶지만, API를 제공하는 측에서 같이 주니까 -> 같은 테이블에

	private String mapY;

	private String mLevel;

	private LocalDateTime modifiedTime;

	@Column(length = 1000)
	private String telephone;

	private String title;

	/**
	 * Creates a new Tour entity from the provided TourResponse DTO and language.
	 *
	 * @param tourResponse the data transfer object containing tour information
	 * @param language the language associated with the tour
	 * @return a new Tour instance populated with values from the DTO and language
	 */
	public static Tour createTour(TourResponse tourResponse, Language language) {

		return Tour.builder()
			.language(language)
			.roadAddress(tourResponse.roadAddress())
			.basicAddress(tourResponse.basicAddress())
			.areaCode(tourResponse.areaCode())
			.sigunGuCode(tourResponse.sigunGuCode())
			.contentId(tourResponse.contentId())
			.contentTypeId(TourType.getTourType(tourResponse.contentTypeId()))
			.eventStartDate(tourResponse.eventStartDate())
			.eventEndDate(tourResponse.eventEndDate())
			.mainImageUrl(tourResponse.mainImageUrl())
			.mapX(tourResponse.mapX())
			.mapY(tourResponse.mapY())
			.mLevel(tourResponse.mLevel())
			.modifiedTime(tourResponse.modifiedTime())
			.telephone(tourResponse.telephone())
			.title(tourResponse.title())
			.build();
	}

	/**
	 * Updates the fields of this Tour entity with values from the provided TourResponse DTO.
	 *
	 * All mutable attributes, including addresses, codes, event dates, image URL, map coordinates, modification time, telephone, and title, are overwritten with the corresponding values from the DTO.
	 */
	public void updateTour(TourResponse tourResponse) {
		roadAddress = tourResponse.roadAddress();
		basicAddress = tourResponse.basicAddress();
		areaCode = tourResponse.areaCode();
		sigunGuCode = tourResponse.sigunGuCode();

		contentTypeId = TourType.valueOf(tourResponse.contentTypeId());

		eventStartDate = tourResponse.eventStartDate();
		eventEndDate = tourResponse.eventEndDate();
		mainImageUrl = tourResponse.mainImageUrl();

		mapX = tourResponse.mapX();
		mapY = tourResponse.mapY();
		mLevel = tourResponse.mLevel();
		modifiedTime = tourResponse.modifiedTime();
		telephone = tourResponse.telephone();
		title = tourResponse.title();
	}

}
