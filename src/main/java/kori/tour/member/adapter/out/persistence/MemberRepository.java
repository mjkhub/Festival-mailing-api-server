package kori.tour.member.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kori.tour.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m JOIN m.subscriptions.subscriptions s "
			+ "WHERE s.areaCode = :areaCode AND s.sigunGuCode = :sigunGuCode order by m.id")
	Slice<Member> findBySubscriptionArea(String areaCode, String sigunGuCode, Pageable pageRequest);

}
