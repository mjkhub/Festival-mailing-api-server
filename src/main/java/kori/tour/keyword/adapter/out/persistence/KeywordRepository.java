package kori.tour.keyword.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import kori.tour.keyword.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

}
