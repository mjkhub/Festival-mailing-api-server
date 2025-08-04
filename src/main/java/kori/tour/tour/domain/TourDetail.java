package kori.tour.tour.domain;

import jakarta.persistence.*;
import kori.tour.tour.domain.dto.TourDetailResponse;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class TourDetail {

	@Id
	@Column(name = "tour_id", nullable = false)
	private Long id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tour_id")
	private Tour tour;

	private String ageLimit;

	private String bookingPlace;

	private String discountInfoFestival;

	private String eventHomepage;

	private String eventPlace; //세부 장소

	@Column(length = 1000)
	private String placeInfo;

	@Column(length = 500)
	private String playTime; // 운영 시간

	@Column(length = 1000)
	private String program;

	private String spendTimeFestival; //예상 소요 시간

	private String sponsor; // sponsor1

	@Column(length = 1000)
	private String sponsorTelephone; // sponsor1

	private String subEvent;

	@Column(length = 500)
	private String useTimeFestival; /**
	 * Creates a new TourDetail entity by mapping fields from the provided TourDetailResponse DTO and associating it with the specified Tour entity.
	 *
	 * @param tourDetailResponse the DTO containing detailed tour information
	 * @param tour the Tour entity to associate with this detail
	 * @return a new TourDetail instance populated with data from the DTO and linked to the given Tour
	 */

	public static TourDetail createTourDetail(TourDetailResponse tourDetailResponse, Tour tour) {

		return TourDetail.builder()
			.tour(tour)
			.ageLimit(tourDetailResponse.ageLimit())
			.bookingPlace(tourDetailResponse.bookingPlace())
			.discountInfoFestival(tourDetailResponse.discountInfoFestival())
			.eventHomepage(tourDetailResponse.eventHomepage())
			.eventPlace(tourDetailResponse.eventPlace())
			.placeInfo(tourDetailResponse.placeInfo())
			.playTime(tourDetailResponse.playTime())
			.program(tourDetailResponse.program())
			.spendTimeFestival(tourDetailResponse.spendTimeFestival())
			.sponsor(tourDetailResponse.sponsor()) // sponsor1
			.sponsorTelephone(tourDetailResponse.sponsorTelephone()) // sponsor1
			.subEvent(tourDetailResponse.subEvent())
			.useTimeFestival(tourDetailResponse.useTimeFestival())
			.build();
	}

	/**
	 * Updates this TourDetail entity's fields with values from the provided TourDetailResponse DTO.
	 *
	 * @param tourDetailResponse the DTO containing updated tour detail information
	 */
	public void updateTourDetail(TourDetailResponse tourDetailResponse) {
		ageLimit = tourDetailResponse.ageLimit();
		bookingPlace = tourDetailResponse.bookingPlace();
		discountInfoFestival = tourDetailResponse.discountInfoFestival();
		eventHomepage = tourDetailResponse.eventHomepage();
		eventPlace = tourDetailResponse.eventPlace();
		placeInfo = tourDetailResponse.placeInfo();
		playTime = tourDetailResponse.playTime();
		program = tourDetailResponse.program();
		spendTimeFestival = tourDetailResponse.spendTimeFestival();
		sponsor = tourDetailResponse.sponsor();
		sponsorTelephone = tourDetailResponse.sponsorTelephone();
		subEvent = tourDetailResponse.subEvent();
		useTimeFestival = tourDetailResponse.useTimeFestival();
	}

}
