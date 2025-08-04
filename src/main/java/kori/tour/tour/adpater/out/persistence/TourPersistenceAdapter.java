package kori.tour.tour.adpater.out.persistence;

import java.util.ArrayList;
import java.util.List;

import kori.tour.common.annotation.PersistenceAdapter;
import kori.tour.tour.application.port.out.TourCrudPort;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourImage;
import kori.tour.tour.domain.TourRepeat;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class TourPersistenceAdapter implements TourCrudPort {

	private final TourRepository tourRepository;

	private final TourDetailRepository tourDetailRepository;

	private final TourRepeatRepository tourRepeatRepository;

	private final TourImageRepository tourImageRepository;

	private final TourJdbcRepository tourJdbcRepository;

	/**
	 * Checks whether a tour with the specified content ID exists in the repository.
	 *
	 * @param tour the tour entity whose content ID is used for the existence check
	 * @return true if a tour with the given content ID exists, false otherwise
	 */
	@Override
	public boolean isExisting(Tour tour) {
		return tourRepository.existsByContentId(tour.getContentId());
	}

	/**
	 * Determines whether the given tour has been updated by checking if a record with the same content ID and modified time exists in the repository.
	 *
	 * @param tour the tour entity to check for updates
	 * @return true if the tour has been updated; false otherwise
	 */
	@Override
	public boolean isUpdated(Tour tour) {
		return tourRepository.isUpdated(tour.getContentId(), tour.getModifiedTime());
	}

	/**
	 * Retrieves a list of tour IDs corresponding to the provided list of content IDs.
	 *
	 * @param contentIdList the list of content IDs for which to find tour IDs
	 * @return a list of tour IDs matching the given content IDs
	 */
	@Override
	public List<Long> findIdListByContentIdList(List<String> contentIdList) {
		return tourRepository.findIdListByContentIdList(contentIdList);
	}

	/**
	 * Deletes tours and all their related entities for the given list of tour IDs.
	 *
	 * For each tour ID provided, removes the corresponding tour, tour details, repeats, and images from the database.
	 *
	 * @param tourIdList list of tour IDs whose tours and related entities should be deleted
	 */
	@Override
	public void deleteToursAndRelatedEntities(List<Long> tourIdList) {
		for (Long tourId : tourIdList) {
			tourRepository.deleteById(tourId);
			tourDetailRepository.deleteByTourId(tourId);
			tourRepeatRepository.deleteAllByTourId(tourId);
			tourImageRepository.deleteAllByTourId(tourId);
		}
	}

	/**
	 * Saves a list of new tours and their related details, repeats, and images in bulk.
	 *
	 * For each new tour, persists the main tour entity and accumulates associated details, repeats, and images,
	 * then performs batch inserts for related entities to optimize database operations.
	 *
	 * @param newTourDtoList the list of new tour data transfer objects to be saved
	 */
	@Override
	public void saveTourListBulk(List<NewTourDto> newTourDtoList) {
		List<TourDetail> tourDetailList = new ArrayList<>();
		List<TourRepeat> tourRepeatList = new ArrayList<>();
		List<TourImage> tourImageList = new ArrayList<>();

		for (NewTourDto newTourDto : newTourDtoList) {
			tourRepository.save(newTourDto.getTour());
			tourDetailList.addAll(newTourDto.getDetailInfo());
			tourRepeatList.addAll(newTourDto.getTourRepeatList());
			tourImageList.addAll(newTourDto.getTourImageList());
		}
		tourJdbcRepository.saveTourDetailInfos(tourDetailList);
		tourJdbcRepository.saveTourRepeats(tourRepeatList);
		tourJdbcRepository.saveTourImages(tourImageList);
	}

}
