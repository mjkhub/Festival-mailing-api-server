package kori.tour.tour.domain;

import jakarta.persistence.*;
import kori.tour.tour.domain.dto.TourRepeatResponse;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class TourRepeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tour_repeat_id")
	private Long id;

	@JoinColumn(name = "tour_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tour tour;

	private String serialNumber;

	private String infoName;

	@Column(length = 4000)
	private String infoText;

	public static TourRepeat createTourRepeat(TourRepeatResponse tourRepeatResponse, Tour tour) {
		return TourRepeat.builder()
			.serialNumber(tourRepeatResponse.serialNumber())
			.infoName(tourRepeatResponse.infoName())
			.infoText(tourRepeatResponse.infoText())
			.tour(tour)
			.build();
	}

}
