package kori.tour.email.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kori.tour.email.domain.EmailContent;

public interface EmailContentRepository extends JpaRepository <EmailContent, Long>{
}
