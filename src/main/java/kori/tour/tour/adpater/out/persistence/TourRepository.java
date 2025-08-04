package kori.tour.tour.adpater.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {

	/**
 * Checks whether a Tour entity exists with the specified contentId.
 *
 * @param contentId the unique content identifier to search for
 * @return true if a Tour with the given contentId exists, false otherwise
 */
boolean existsByContentId(String contentId);

	/**
	 * Deletes the Tour entity with the specified ID from the database.
	 *
	 * @param id the unique identifier of the Tour to delete
	 */
	@Modifying
	@Query("delete from Tour t where t.id =:id")
	void deleteById(Long id);

	/**
	 * Retrieves the IDs of all Tour entities whose contentId is included in the specified list.
	 *
	 * @param contentIdList a list of contentId values to match against Tour entities
	 * @return a list of Tour entity IDs corresponding to the provided contentId values
	 */
	@Query("select t.id from Tour t where t.contentId in :contentIdList")
	List<Long> findIdListByContentIdList(List<String> contentIdList);

	/**
	 * Determines whether a Tour with the specified contentId exists and has a modifiedTime earlier than the given value.
	 *
	 * @param contentId the unique identifier of the Tour
	 * @param modifiedTime the timestamp to compare against the Tour's modifiedTime
	 * @return true if such a Tour exists and its modifiedTime is earlier than the specified value; false otherwise
	 */
	@Query("select case when count(t) > 0 then true else false end from Tour t "
			+ "where t.contentId = :contentId and t.modifiedTime < :modifiedTime")
	boolean isUpdated(String contentId, LocalDateTime modifiedTime);



}
