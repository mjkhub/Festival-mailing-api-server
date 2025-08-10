package kori.tour.tour.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {

	boolean existsByContentId(String contentId);

	@Query("select t.id from Tour t where t.contentId in :contentIdList")
	List<Long> findIdListByContentIdList(List<String> contentIdList);

	@Modifying
	@Query("delete from Tour t where t.id =:id")
	void deleteById(Long id);

	@Query("select case when count(t) > 0 then true else false end from Tour t "
			+ "where t.contentId = :contentId and t.modifiedTime < :modifiedTime")
	boolean isUpdated(String contentId, LocalDateTime modifiedTime);

}
