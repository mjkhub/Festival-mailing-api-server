package kori.tour.tour.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.transaction.Transactional;
import kori.tour.tour.domain.Tour;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TourRepositoryTest {

	@Autowired
	TourRepository tourRepository;

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
	@DisplayName("contentId 리스트로 투어의 PK 리스트 조회하기")
	void test() {
		// Given
		Tour tour = Tour.builder().contentId("3345700").build();
		tourRepository.save(tour);
		List<String> contentIdList = List.of("3345700");

		// When
		List<Long> idList = tourRepository.findIdListByContentIdList(contentIdList);

		// Then
		assertThat(idList.size()).isEqualTo(1);
	}


}
