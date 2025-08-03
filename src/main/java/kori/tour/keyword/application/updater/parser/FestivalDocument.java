package kori.tour.keyword.application.updater.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourRepeat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FestivalDocument {

	private String festivalName;

	private String programs;

	private String eventLocation;

	private String eventHours;

	private String duration;

	private String admissionFee;

	private String ageLimit;

	private Map<String, String> festivalDetails;

	public static FestivalDocument createFestivalDocument(NewTourDto newTourDto) {

		Tour tour = newTourDto.getTour();
		TourDetail tourDetail = newTourDto.getDetailInfo().get(0);
		List<TourRepeat> tourRepeat = newTourDto.getTourRepeatList();

		String festivalName = tour.getTitle();
		String programs = tourDetail.getProgram() + tourDetail.getSubEvent();
		String eventLocation = tourDetail.getEventPlace();
		String eventHours = tourDetail.getSpendTimeFestival();
		String duration = tourDetail.getPlayTime();
		String admissionFee = tourDetail.getUseTimeFestival();
		String ageLimit = tourDetail.getAgeLimit();
		Map<String, String> festivalDetails = new HashMap<>();
		for (TourRepeat repeat : tourRepeat)
			festivalDetails.put(repeat.getInfoName(), repeat.getInfoText());

		return new FestivalDocument(festivalName, programs, eventLocation, eventHours, duration, admissionFee, ageLimit,
				festivalDetails);
	}

}
