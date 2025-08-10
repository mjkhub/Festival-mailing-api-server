package kori.tour.tour.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kori.tour.tour.domain.Tour;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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

}
