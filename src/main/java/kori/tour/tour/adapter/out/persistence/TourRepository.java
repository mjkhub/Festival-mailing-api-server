package kori.tour.tour.adapter.out.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import kori.tour.tour.domain.RegionCode;
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

	@Query("select t.id from Tour t where t.regionCode in :subs and t.eventEndDate >= :now order by t.eventStartDate asc, t.id asc")
	Slice<Long> findTourIdListByMemberSubscriptions(List<RegionCode> subs, LocalDate now, Pageable pageable);

	@Query("select t from Tour t left join fetch t.keywords.keywordSet kw where t.id in :ids order by t.eventStartDate asc, t.id asc")
	List<Tour> findWithKeywordsByIds(List<Long> ids);

	@Query("select t from Tour t left join fetch  t.keywords.keywordSet kw  where t.id =:id ")
	Optional<Tour> findWithKeywordsById(Long id);


}
