package kori.tour.tour.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourRepeat;

public interface TourRepeatRepository extends JpaRepository<TourRepeat, Long> {

	@Modifying
	@Query("delete from TourRepeat tr where tr.tour.id =:tourId")
	void deleteAllByTourId(Long tourId);

	List<TourRepeat> findByTourId(Long tourId);

}
