package kori.tour.tour.domain;

import jakarta.persistence.*;
import kori.tour.tour.domain.dto.TourImageResponse;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class TourImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tour_image_id")
	private Long id;

	@JoinColumn(name = "tour_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tour tour;

	private String originImageUrl;

	private String smallImageUrl;

	private String imageName;

	private String serialNumber;

	/**
	 * Creates a new TourImage entity from the provided TourImageResponse DTO and Tour entity.
	 *
	 * @param tourImageResponse the DTO containing image details to be mapped
	 * @param tour the Tour entity to associate with the image
	 * @return a new TourImage instance populated with data from the DTO and associated Tour
	 */
	public static TourImage createTourImage(TourImageResponse tourImageResponse, Tour tour) {
		return TourImage.builder()
			.originImageUrl(tourImageResponse.originImageUrl())
			.smallImageUrl(tourImageResponse.smallImageUrl())
			.imageName(tourImageResponse.imageName())
			.serialNumber(tourImageResponse.serialNum())
			.tour(tour)
			.build();
	}

}
