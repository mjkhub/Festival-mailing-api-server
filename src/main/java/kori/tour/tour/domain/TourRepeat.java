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

	/**
	 * Creates a new TourRepeat entity from the provided TourRepeatResponse DTO and associates it with the given Tour.
	 *
	 * @param tourRepeatResponse the DTO containing serial number, info name, and info text for the TourRepeat
	 * @param tour the Tour entity to associate with the new TourRepeat
	 * @return a new TourRepeat entity populated with data from the DTO and linked to the specified Tour
	 */
	public static TourRepeat createTourRepeat(TourRepeatResponse tourRepeatResponse, Tour tour) {
		return TourRepeat.builder()
			.serialNumber(tourRepeatResponse.serialNumber())
			.infoName(tourRepeatResponse.infoName())
			.infoText(tourRepeatResponse.infoText())
			.tour(tour)
			.build();
	}

}
