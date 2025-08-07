package kori.tour.member.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kori.tour.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m JOIN m.subscriptions.subscriptions s "
			+ "WHERE s.areaCode = :areaCode AND s.sigunGuCode = :sigunGuCode")
	List<Member> findBySubscriptionArea(String areaCode, String sigunGuCode);

}
