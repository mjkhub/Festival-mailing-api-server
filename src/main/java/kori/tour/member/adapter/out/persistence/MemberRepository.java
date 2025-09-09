package kori.tour.member.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformType;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m JOIN m.subscriptions.subscriptions s "
			+ "WHERE s.areaCode = :areaCode AND s.sigunGuCode = :sigunGuCode order by m.id")
	Slice<Member> findBySubscriptionArea(String areaCode, String sigunGuCode, Pageable pageRequest);

	@Query("SELECT m FROM Member m LEFT JOIN FETCH m.subscriptions.subscriptions s WHERE m.id = :memberId")
	Optional<Member> findByIdWithSubscriptions(Long memberId);


	@Query("select m from Member m where m.platformInfo.platformType =:platformType and m.platformInfo.platformPk =:platformPk and m.platformInfo.platformEmail =:platformEmail")
	Optional<Member> findByPlatformProfile(PlatformType platformType, String platformPk, String platformEmail);


}
