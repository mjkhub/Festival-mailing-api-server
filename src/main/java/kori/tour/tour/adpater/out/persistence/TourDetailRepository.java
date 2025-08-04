package kori.tour.tour.adpater.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourDetail;

interface TourDetailRepository extends JpaRepository<TourDetail, Long> {

	/**
	 * Deletes all TourDetail entities associated with the specified tour ID.
	 *
	 * @param tourId the ID of the tour whose details should be deleted
	 */
	@Modifying
	@Query("delete from TourDetail td where td.tour.id =:tourId")
	void deleteByTourId(Long tourId);

}
