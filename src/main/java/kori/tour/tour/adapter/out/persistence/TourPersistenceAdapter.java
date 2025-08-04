package kori.tour.tour.adapter.out.persistence;

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

	@Override
	public boolean isExisting(Tour tour) {
		return tourRepository.existsByContentId(tour.getContentId());
	}

	@Override
	public boolean isUpdated(Tour tour) {
		return tourRepository.isUpdated(tour.getContentId(), tour.getModifiedTime());
	}

	@Override
	public List<Long> findIdListByContentIdList(List<String> contentIdList) {
		return tourRepository.findIdListByContentIdList(contentIdList);
	}

	/**
	 * 정보가 업데이트 되어서 기존의 데이터를 삭제하는 경우는 잘 없어서 그냥 간단하게 구현
	 * */
	@Override
	public void deleteToursAndRelatedEntities(List<Long> tourIdList) {
		for (Long tourId : tourIdList) {
			tourRepository.deleteById(tourId);
			tourDetailRepository.deleteByTourId(tourId);
			tourRepeatRepository.deleteAllByTourId(tourId);
			tourImageRepository.deleteAllByTourId(tourId);
		}
	}

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
