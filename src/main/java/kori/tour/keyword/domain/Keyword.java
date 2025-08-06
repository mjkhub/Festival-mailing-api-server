package kori.tour.keyword.domain;

import jakarta.persistence.*;
import kori.tour.tour.domain.Tour;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "keyword_id")
	private Long id;

	@JoinColumn(name = "tour_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tour tour;

	private String keyword;

}
