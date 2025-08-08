package kori.tour.tour.application.updater;

import static kori.tour.global.utils.CollectionUtils.*;

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
class TourUpdateService {

// DB를 포함한 로직 및 저장 담당

	private final TourCrudPort tourCrudPort;

	public TourFilterResponse separateUpdateTourNewTour(List<Tour> tours, Language language) {
		Map<Boolean, List<Tour>> separatedTours = separateList(tours, tourCrudPort::isExisting);
		List<Tour> newTours = getFromMap(separatedTours, false);
		List<Tour> updatedTours = filterList(getFromMap(separatedTours, true), tourCrudPort::isUpdated);
		log.info("언어={} 총 응답 데이터={} 새로운 투어={} 업데이트 투어={} 분리 완료", language.getKrName(), tours.size(), newTours.size(),
				updatedTours.size());
		return new TourFilterResponse(newTours, updatedTours);
	}

	@Transactional
	public TourApiResponse updateTours(TourApiResponse tourApiResponse, Language language) {
		List<NewTourDto> updatedTourDtoList = tourApiResponse.updatedToursEntity();

		List<Tour> updatedTours = mapList(updatedTourDtoList, NewTourDto::getTour);
		List<String> contentIdList = mapList(updatedTours, Tour::getContentId);
		List<Long> updatedToursPk = tourCrudPort.findIdListByContentIdList(contentIdList);
		tourCrudPort.deleteToursAndRelatedEntities(updatedToursPk);

		log.info("언어={} 응답 결과: 업데이트된 투어={} 삭제 완료 ", language.getKrName(), updatedTours.size());
		return tourApiResponse;
	}

	@Transactional
	public TourApiResponse saveNewTours(TourApiResponse tourApiResponse, Language language) {
		tourCrudPort.saveTourListBulk(tourApiResponse.newToursEntity());
		tourCrudPort.saveTourListBulk(tourApiResponse.newToursEntity());
		log.info("언어={} 응답 결과: 새로운 투어={} 업데이트된 투어={} 저장 완료", language.getKrName(),
				tourApiResponse.newToursEntity().size(), tourApiResponse.updatedToursEntity().size());
		return tourApiResponse;
	}

	public void sumTourEntity(AtomicInteger count, TourApiResponse tourApiResponse) {
		count.accumulateAndGet(tourApiResponse.newToursEntity().size(), Integer::sum);
		count.accumulateAndGet(tourApiResponse.updatedToursEntity().size(), Integer::sum);
	}

}
