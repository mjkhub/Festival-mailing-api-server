package kori.tour.tour.adpater.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.TourRepeat;

interface TourRepeatRepository extends JpaRepository<TourRepeat, Long> {

	/**
	 * Deletes all {@code TourRepeat} entities associated with the specified tour ID.
	 *
	 * @param tourId the ID of the tour whose repeat records should be deleted
	 */
	@Modifying
	@Query("delete from TourRepeat tr where tr.tour.id =:tourId")
	void deleteAllByTourId(Long tourId);

}
