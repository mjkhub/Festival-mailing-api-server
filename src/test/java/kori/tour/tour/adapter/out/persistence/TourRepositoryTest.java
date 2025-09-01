package kori.tour.tour.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kori.tour.member.domain.Subscription;
import kori.tour.tour.domain.RegionCode;
import kori.tour.tour.domain.Tour;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TourRepositoryTest {

	@Autowired
	TourRepository tourRepository;

	@Autowired
	EntityManager em;

	@Test
	@DisplayName("ID에 해당하는 Tour 데이터를 성공적으로 삭제하는지 확인")
	void deleteById() {
		// given
		Tour tour = Tour.builder()
				.contentId("112233")
				.modifiedTime(LocalDateTime.now())
				.build();
		tour = tourRepository.save(tour);
		em.flush();
		em.clear();

		// when
		tourRepository.deleteById(tour.getId());

		// then
		assertThat(tourRepository.findById(tour.getId())).isEmpty();
	}


	@Test
	@DisplayName("기존에 저장된 투어 정보에 업데이트가 발생했을 때")
	void isUpdated() {
		// given
		String contentId = "3115679";
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		Tour tour = Tour.builder().contentId(contentId).modifiedTime(yesterday).build();
		tourRepository.save(tour);

		// when
		boolean isUpdated = tourRepository.isUpdated(contentId, LocalDateTime.now());

		// then
		assertThat(isUpdated).isTrue();
	}

	@Test
	@DisplayName("구독 지역(areaCode,sigunGuCode)에 해당하고 아직 종료되지 않은 Tour만 시작일 오름차순으로 Slice 페이징 조회")
	void findByMemberSubscriptions_filtersAndSortsAndPaginates() {
		// given
		String subArea = "11";
		String subSigungu = "110";
		List<RegionCode> subscriptions = List.of(
				Subscription.builder()
						.areaCode(subArea)
						.sigunGuCode(subSigungu)
						.build()).stream()
				.map(Subscription::mapToRegionCode)
				.toList();

		LocalDate now = LocalDate.now();

		// 구독 지역 + 진행/예정 (포함되어야 함)
		Tour t1 = Tour.builder()
				.contentId("A-keep-1")
				.regionCode(new RegionCode(subArea,subSigungu))
				.eventStartDate(now.plusDays(1))   // 더 이른 시작
				.eventEndDate(now.plusDays(10))    // 아직 안 끝남
				.build();

		// 구독 지역 + 진행/예정 (포함되어야 함)
		Tour t2 = Tour.builder()
				.contentId("B-keep-2")
				.regionCode(new RegionCode(subArea,subSigungu))
				.eventStartDate(now.plusDays(3))   // 더 늦은 시작
				.eventEndDate(now.plusDays(7))     // 아직 안 끝남
				.build();

		// 구독 지역이지만 이미 종료됨 (제외)
		Tour t3 = Tour.builder()
				.contentId("C-exclude-ended")
				.regionCode(new RegionCode(subArea,subSigungu))
				.eventStartDate(now.minusDays(10))
				.eventEndDate(now.minusDays(1))    // 끝남
				.build();

		// 다른 지역 (제외)
		Tour t4 = Tour.builder()
				.contentId("D-exclude-other-region")
				.regionCode(new RegionCode("99","990"))
				.eventStartDate(now.plusDays(2))
				.eventEndDate(now.plusDays(9))
				.build();

		tourRepository.saveAll(List.of(t1, t2, t3, t4));
		em.flush();
		em.clear();

		// when: 페이지 크기 1로 잘리는지, 정렬이 시작일 asc로 되는지 확인
		Pageable firstPage = PageRequest.of(0, 1);
		Slice<Tour> slice1 = tourRepository.findByMemberSubscriptions(subscriptions, now, firstPage);

		// then: 필터링 결과 2건(t1,t2) 중 시작일 빠른 t1이 먼저, hasNext=true
		assertThat(slice1.getContent())
				.extracting(Tour::getContentId)
				.containsExactly("A-keep-1");
		assertThat(slice1.hasNext()).isTrue();

		// when: 다음 페이지
		Pageable secondPage = PageRequest.of(1, 1);
		Slice<Tour> slice2 = tourRepository.findByMemberSubscriptions(subscriptions, now, secondPage);

		// then: 두 번째는 t2, hasNext=false
		assertThat(slice2.getContent())
				.extracting(Tour::getContentId)
				.containsExactly("B-keep-2");
		assertThat(slice2.hasNext()).isFalse();
	}

}
