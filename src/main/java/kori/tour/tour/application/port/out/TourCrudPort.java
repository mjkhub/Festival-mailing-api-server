package kori.tour.tour.application.port.out;

import java.util.List;

import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;

public interface TourCrudPort {

	boolean isExisting(Tour tour);

	boolean isUpdated(Tour tour);

	List<Long> findIdListByContentIdList(List<String> contentIdList);

	void deleteToursAndRelatedEntities(List<Long> tourIdList);

	void saveTourListBulk(List<NewTourDto> newTourDtoList);

}
