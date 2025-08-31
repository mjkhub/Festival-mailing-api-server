package kori.tour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import kori.tour.member.domain.*;
import kori.tour.tour.domain.*;
import kori.tour.tour.domain.dto.TourResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
public class TourApplication {

	private final EntityManager em;

	private static final String AREA_CODE_SEOUL = "1";
	private static final String SIGUNGU_CODE_GANGNAM = "1";


	public static void main(String[] args) {
		SpringApplication.run(TourApplication.class, args);
	}

	@PostConstruct
	public void setTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.info("Application TimeZone = {}", TimeZone.getDefault());
	}

	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void init() {
		// 이미 존재하면 스킵
		Long exists = em.createQuery(
						"select count(t) from Tour t where t.contentId = :cid", Long.class)
				.setParameter("cid", "CNT-SEOUL-GANGNAM-001")
				.getSingleResult();
		if (exists != null && exists > 0) return;

		// 1) Member
		Member member = Member.builder()
				.platformInfo(new PlatformInfo(
						PlatformType.GOOGLE,
						"google-1234567890",
						"demo.user@example.com"
				))
				.activityInfo(new ActivityInfo(
						LocalDateTime.now().minusDays(1),
						LocalDate.now()
				))
				.emailSubscribe(Boolean.TRUE)
				.build();
		em.persist(member);

		// 2) TourResponse → Tour.createTour() 사용
		TourResponse tourResponse = new TourResponse(
				"서울특별시 강남구 테헤란로 123",   // roadAddress
				"삼성동 코엑스 앞 광장",          // basicAddress
				AREA_CODE_SEOUL,                 // areaCode
				SIGUNGU_CODE_GANGNAM,            // sigunguCode
				"CNT-SEOUL-GANGNAM-001",         // contentId
				"85",                            // contentTypeId (Festival)
				LocalDate.now().plusDays(3),     // eventStartDate
				LocalDate.now().plusDays(5),     // eventEndDate
				"https://example.com/images/gangnam-festival.jpg", // mainImageUrl
				"127.062",                       // mapX
				"37.509",                        // mapY
				"7",                             // mLevel
				LocalDateTime.now(),             // modifiedTime
				"02-123-4567",                   // telephone
				"강남 여름 페스티벌"               // title
		);

		Tour tour = Tour.createTour(tourResponse, Language.KOREAN);
		tour.addKeywords(Set.of("여름축제", "강남"));
		em.persist(tour);

		// 3) TourDetail
		TourDetail detail = TourDetail.builder()
				.tour(tour)
				.ageLimit("전연령")
				.bookingPlace("현장 및 온라인 예매")
				.discountInfoFestival("강남구민 10% 할인")
				.eventHomepage("https://example.com/gangnam-festival")
				.eventPlace("코엑스 앞 광장")
				.placeInfo("지하철 2호선 삼성역 5번 출구 연결")
				.playTime("18:00 ~ 22:00")
				.program("K-POP 공연, 푸드존, 체험 부스")
				.spendTimeFestival("평균 2~3시간")
				.sponsor("강남구청")
				.sponsorTelephone("02-123-4567")
				.subEvent("불꽃놀이")
				.useTimeFestival("무료 입장")
				.build();
		em.persist(detail);

		// 4) TourImage
		em.persist(TourImage.builder()
				.tour(tour)
				.originImageUrl("https://example.com/images/gangnam-1.jpg")
				.smallImageUrl("https://example.com/images/gangnam-1_small.jpg")
				.imageName("gangnam-1")
				.serialNumber("IMG-001")
				.build());

		// 5) TourRepeat
		em.persist(TourRepeat.builder()
				.tour(tour)
				.serialNumber("REP-001")
				.infoName("안내 사항")
				.infoText("대중교통 이용 권장, 우천 시 일부 프로그램 취소 가능")
				.build());

		// 6) Member 구독
		member.addSubscription(Subscription.builder()
				.areaCode(AREA_CODE_SEOUL)
				.sigunGuCode(SIGUNGU_CODE_GANGNAM)
				.subscribeDate(LocalDateTime.now())
				.build());
	}

}
