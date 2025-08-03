package kori.tour.tour.adpater.out.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourType;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TourJdbcRepositoryTest {

	@Autowired
	TourJdbcRepository tourJdbcRepository;

	@Autowired
	TourRepository tourJpaRepository;

	static List<Tour> tours = new ArrayList<>();

	@BeforeAll
	static void initMockData() {
		for (int i = 30_000; i <= 31_000; i++) {
			Tour tour = getMockTour("contentId" + i);
			tours.add(tour);
		}
	}

	@Test
	@DisplayName("투어 정보 저장: 배치 삽입")
	void JdbcTemplate_BatchInsert() {
		tourJdbcRepository.saveTours(tours);
	}

	@Test
	@DisplayName("투어 정보 저장: 단일 insert 쿼리")
	void JPA_saveAll() {
		tourJpaRepository.saveAll(tours);
	}

	private static Tour getMockTour(String contentId) {
		return Tour.builder()
			.language(Language.KOREAN)
			.roadAddress("123 Road St.")
			.basicAddress("456 Basic Ave.")
			.areaCode("01")
			.sigunGuCode("001")
			.contentId(contentId)
			.contentTypeId(TourType.FESTIVAL)
			.eventStartDate(LocalDate.of(2024, 1, 1))
			.eventEndDate(LocalDate.of(2024, 1, 10))
			.mainImageUrl("http://example.com/image1.jpg")
			.mapX("127.12345")
			.mapY("37.54321")
			.mLevel("5")
			.modifiedTime(LocalDateTime.now())
			.telephone("010-1234-5678")
			.title("Sample Tour 1")
			.build();
	}

}
