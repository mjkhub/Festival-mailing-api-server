package kori.tour.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kori.tour.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
