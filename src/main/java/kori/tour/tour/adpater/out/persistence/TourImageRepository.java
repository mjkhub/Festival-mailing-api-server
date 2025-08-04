package kori.tour.tour.adpater.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourImage;

interface TourImageRepository extends JpaRepository<TourImage, Long> {

	/**
	 * Deletes all TourImage entities associated with the specified tour ID.
	 *
	 * @param tourId the ID of the tour whose images should be deleted
	 */
	@Modifying
	@Query("delete from TourImage ti where ti.tour.id =:tourId")
	void deleteAllByTourId(Long tourId);

}
