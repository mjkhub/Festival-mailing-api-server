package kori.tour.tour.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourDetail;

public interface TourDetailRepository extends JpaRepository<TourDetail, Long> {

	@Modifying
	@Query("delete from TourDetail td where td.tour.id =:tourId")
	void deleteByTourId(Long tourId);

	Optional<TourDetail> findByTourId(Long tourId);

}
