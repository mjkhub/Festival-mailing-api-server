package kori.tour.tour.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourImage;

interface TourImageRepository extends JpaRepository<TourImage, Long> {

	@Modifying
	@Query("delete from TourImage ti where ti.tour.id =:tourId")
	void deleteAllByTourId(Long tourId);

}
