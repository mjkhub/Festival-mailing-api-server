package kori.tour.tour.application.port.out;

import java.util.List;

import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;

public interface TourCrudPort {

	/**
 * Checks whether the specified tour entity already exists in the data store.
 *
 * @param tour the tour entity to check for existence
 * @return true if the tour exists, false otherwise
 */
boolean isExisting(Tour tour);

	/**
 * Determines whether the specified tour entity has been updated.
 *
 * @param tour the tour entity to check
 * @return true if the tour has been updated; false otherwise
 */
boolean isUpdated(Tour tour);

	/**
 * Retrieves a list of tour IDs that correspond to the provided list of content IDs.
 *
 * @param contentIdList the list of content IDs to search for
 * @return a list of tour IDs matching the given content IDs
 */
List<Long> findIdListByContentIdList(List<String> contentIdList);

	/**
 * Deletes tours and their associated entities for the specified list of tour IDs.
 *
 * @param tourIdList the list of tour IDs whose tours and related entities should be deleted
 */
void deleteToursAndRelatedEntities(List<Long> tourIdList);

	/**
 * Persists a list of new tours in bulk.
 *
 * @param newTourDtoList the list of new tour data transfer objects to be saved
 */
void saveTourListBulk(List<NewTourDto> newTourDtoList);

}
