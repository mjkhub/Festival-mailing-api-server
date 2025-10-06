package kori.tour.tour.adapter.in;

import java.util.ArrayList;
import java.util.List;

import kori.tour.tour.application.updater.TourService;
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

    private final TourService tourService;

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
        return ResponseEntity.ok().body(tourService.getTourAllDto(tourId));
    }

}
