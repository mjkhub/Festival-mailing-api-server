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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tour_detail_id")
	private Long id;

	@JoinColumn(name = "tour_id", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Tour tour;

	private String ageLimit;

	private String bookingPlace;

	private String discountInfoFestival;

	@Column(length = 1500)
	private String eventHomepage;

	private String eventPlace; // 세부 장소

	@Column(length = 1000)
	private String placeInfo;

	@Column(length = 500)
	private String playTime; // 운영 시간

	@Column(length = 1000)
	private String program;

	private String spendTimeFestival; // 예상 소요 시간

	private String sponsor; // sponsor1

	@Column(length = 1000)
	private String sponsorTelephone; // sponsor1

	private String subEvent;

	@Column(length = 500)
	private String useTimeFestival; // 비용

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
	 * 투어를 갱신해야할 때, 관련 엔티티를 모두 삭제하는 Hard delete 방식을 채택하면서
	 * 사용하지 않게 된 메소드 2025.09.04
	 * */
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
