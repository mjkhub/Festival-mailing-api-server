package kori.tour.tour.application.updater;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.tour.adapter.in.api.TourAllDto;
import kori.tour.tour.adapter.out.persistence.TourDetailRepository;
import kori.tour.tour.adapter.out.persistence.TourImageRepository;
import kori.tour.tour.adapter.out.persistence.TourRepeatRepository;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourImage;
import kori.tour.tour.domain.TourRepeat;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourDetailRepository tourDetailRepository;
    private final TourRepeatRepository tourRepeatRepository;
    private final TourImageRepository tourImageRepository;

    @Cacheable(value = "tourAll", key = "#tourId", unless = "#result == null")
    public TourAllDto getTourAllDto(Long tourId){
        Tour tour = tourRepository.findWithKeywordsById(tourId).orElseThrow(() -> new NotFoundException(ErrorCode.TOUR_NOT_FOUND));
        TourDetail tourDetail = tourDetailRepository.findByTourId(tourId).orElseGet( ()-> null);
        List<TourRepeat> tourRepeats = tourRepeatRepository.findByTourId(tourId);
        List<TourImage> tourImages = tourImageRepository.findByTourId(tourId);
        return mapToTourAllDto(tour, tourDetail, tourRepeats, tourImages);
    }

    private TourAllDto mapToTourAllDto(
            Tour tour,
            TourDetail tourDetail,
            List<TourRepeat> tourRepeats,
            List<TourImage> tourImages
    ) {
        // 키워드: Set -> List (그대로)
        List<String> keywords = new ArrayList<>(tour.getAllKeywords());

        // 이미지: 대표 + 각 이미지의 원본/스몰 모두 추가 (필터링/중복제거/정렬 없음)
        List<String> images = new ArrayList<>();
        images.add(tour.getMainImageUrl());
        for (TourImage img : tourImages) images.add(img.getOriginImageUrl());


        // 개요
        TourAllDto.Overview overview = new TourAllDto.Overview(
                String.valueOf(tour.getEventStartDate()),
                String.valueOf(tour.getEventEndDate()),
                tourDetail != null ? tourDetail.getPlayTime() : null,
                tourDetail != null ? tourDetail.getSpendTimeFestival() : null,
                tourDetail != null ? tourDetail.getUseTimeFestival() : null);

        // 상세
        List<TourAllDto.Info> infoList = new ArrayList<>();
        for (TourRepeat r : tourRepeats)
            infoList.add(new TourAllDto.Info(r.getInfoName(), r.getInfoText()));

        // 오시는 길
        TourAllDto.Directions directions = new TourAllDto.Directions(
                tour.getRoadAddress(),
                tourDetail != null ? tourDetail.getEventPlace() : null,
                tour.getTelephone()
        );

        // 레코드 한 번에 생성
        return new TourAllDto(
                tour.getId(),
                tour.getTitle(),
                keywords,
                images,
                overview,
                infoList,
                directions);
    }
}
