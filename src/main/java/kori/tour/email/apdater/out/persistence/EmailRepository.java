package kori.tour.email.apdater.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kori.tour.email.domain.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
