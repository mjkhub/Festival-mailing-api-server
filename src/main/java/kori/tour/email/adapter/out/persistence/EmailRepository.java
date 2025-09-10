package kori.tour.email.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kori.tour.email.domain.Email;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmailRepository extends JpaRepository<Email, Long> {

    void deleteByMember_Id(Long memberId);
}
