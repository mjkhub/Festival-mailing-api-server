package kori.tour.tour.application.updater.dto;

import java.util.List;

import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourImage;
import kori.tour.tour.domain.TourRepeat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewTourDto {

	private Tour tour;

	private List<TourDetail> detailInfo;

	private List<TourRepeat> tourRepeatList;

	private List<TourImage> tourImageList;

}
