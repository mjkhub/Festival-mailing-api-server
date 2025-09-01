package kori.tour.tour.adapter.in;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value= "/api/tours", produces = MediaType.APPLICATION_JSON_VALUE)
public class TourController {

    private final TourRepository tourRepository;
    private final TourDetailRepository tourDetailRepository;
    private final TourRepeatRepository tourRepeatRepository;
    private final TourImageRepository tourImageRepository;

    @Operation(summary = "축제/행사 상세 정보 조회", description = "특정 축제 또는 행사의 모든 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TourAllDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 축제/행사 정보를 찾을 수 없습니다.",
                    content = @Content)
    })
    @GetMapping("/{tourId}")
    public ResponseEntity<TourAllDto> getAllTourInfo(@PathVariable Long tourId){
        Tour tour = tourRepository.findWithKeywordsById(tourId).orElseThrow(() -> new NotFoundException(ErrorCode.TOUR_NOT_FOUND));
        TourDetail tourDetail = tourDetailRepository.findByTourId(tourId).orElseThrow(() -> new NotFoundException(ErrorCode.TOUR_NOT_FOUND));
        List<TourRepeat> tourRepeats = tourRepeatRepository.findByTourId(tourId);
        List<TourImage> tourImages = tourImageRepository.findByTourId(tourId);
        return ResponseEntity.ok().body(mapToTourAllDto(tour, tourDetail, tourRepeats, tourImages));
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
                tour.getEventStartDate().toString(),
                tour.getEventEndDate().toString(),
                tourDetail.getPlayTime(),
                tourDetail.getSpendTimeFestival(),
                tourDetail.getUseTimeFestival());

        // 상세
        List<TourAllDto.Info> infoList = new ArrayList<>();
        for (TourRepeat r : tourRepeats)
            infoList.add(new TourAllDto.Info(r.getInfoName(), r.getInfoText()));

        TourAllDto.Detail detail = new TourAllDto.Detail(infoList);

        // 오시는 길
        TourAllDto.Directions directions = new TourAllDto.Directions(
                tour.getRoadAddress(),
                tourDetail.getEventPlace(),
                tour.getTelephone());

        // 레코드 한 번에 생성
        return new TourAllDto(
                tour.getId(),
                tour.getTitle(),
                keywords,
                images,
                overview,
                detail,
                directions);
    }


}
