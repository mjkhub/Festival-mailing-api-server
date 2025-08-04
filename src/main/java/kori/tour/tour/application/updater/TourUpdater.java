package kori.tour.tour.application.updater;

import static kori.tour.common.utils.CollectionUtils.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.tour.application.port.out.TourCrudPort;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.application.updater.dto.TourApiResponse;
import kori.tour.tour.application.updater.dto.TourFilterResponse;
import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class TourUpdater { // DB를 포함한 로직 및 저장 담당

	private final TourCrudPort tourCrudPort;

	/**
	 * Separates a list of tours into new and updated tours based on their existence and update status in the database.
	 *
	 * @param tours the list of tours to be processed
	 * @param language the language context for logging purposes
	 * @return a {@link TourFilterResponse} containing lists of new and updated tours
	 */
	public TourFilterResponse separateUpdateTourNewTour(List<Tour> tours, Language language) {
		Map<Boolean, List<Tour>> separatedTours = separateList(tours, tourCrudPort::isExisting);
		List<Tour> newTours = getFromMap(separatedTours, false);
		List<Tour> updatedTours = filterList(getFromMap(separatedTours, true), tourCrudPort::isUpdated);
		log.info("언어={} 총 응답 데이터={} 새로운 투어={} 업데이트 투어={} 분리 완료", language.getKrName(), tours.size(), newTours.size(),
				updatedTours.size());
		return new TourFilterResponse(newTours, updatedTours);
	}

	/**
	 * Updates existing tours and their related entities in the database based on the provided API response and language.
	 *
	 * Deletes tours and their associated data matching the updated content IDs, then bulk saves the updated tour information.
	 *
	 * @param tourApiResponse the API response containing updated tour data
	 * @param language the language context for the update operation
	 * @return the original API response after performing the update
	 */
	@Transactional
	public TourApiResponse updateTours(TourApiResponse tourApiResponse, Language language) {
		List<NewTourDto> updatedTourDtoList = tourApiResponse.updatedToursEntity();

		List<Tour> updatedTours = mapList(updatedTourDtoList, NewTourDto::getTour);
		List<String> contentIdList = mapList(updatedTours, Tour::getContentId);
		List<Long> updatedToursPk = tourCrudPort.findIdListByContentIdList(contentIdList);
		tourCrudPort.deleteToursAndRelatedEntities(updatedToursPk);

		tourCrudPort.saveTourListBulk(updatedTourDtoList);
		log.info("언어={} 응답 결과: 업데이트 투어={} 업데이트 완료", language.getKrName(), updatedTours.size());
		return tourApiResponse;
	}

	/**
	 * Saves new tour entities in bulk to the database for the specified language.
	 *
	 * @param tourApiResponse the API response containing new tour entities to be saved
	 * @param language the language context for the tours
	 * @return the original API response after saving the new tours
	 */
	@Transactional
	public TourApiResponse saveNewTours(TourApiResponse tourApiResponse, Language language) {
		tourCrudPort.saveTourListBulk(tourApiResponse.newToursEntity());
		log.info("언어={} 응답 결과: 새로운 투어={} 저장 완료", language.getKrName(), tourApiResponse.newToursEntity().size());
		return tourApiResponse;
	}

	/**
	 * Adds the counts of new and updated tour entities from the given API response to the provided atomic counter.
	 *
	 * @param count the atomic integer to accumulate the total number of new and updated tours
	 * @param tourApiResponse the API response containing lists of new and updated tour entities
	 */
	public void sumTourEntity(AtomicInteger count, TourApiResponse tourApiResponse) {
		count.accumulateAndGet(tourApiResponse.newToursEntity().size(), Integer::sum);
		count.accumulateAndGet(tourApiResponse.updatedToursEntity().size(), Integer::sum);
	}

}
