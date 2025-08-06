package kori.tour.tour.application.updater;

import static kori.tour.common.utils.CollectionUtils.mapList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.application.updater.dto.TourApiResponse;
import kori.tour.tour.application.updater.dto.TourFilterResponse;
import kori.tour.tour.application.updater.dto.TourMetaData;
import kori.tour.tour.application.updater.parser.TourApiResponseParser;
import kori.tour.tour.domain.*;
import kori.tour.tour.domain.dto.TourDetailResponse;
import kori.tour.tour.domain.dto.TourImageResponse;
import kori.tour.tour.domain.dto.TourRepeatResponse;
import kori.tour.tour.domain.dto.TourResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiClient {

// API 통신 담당

	private final ApiResolver apiResolver;

	private final RestTemplate restTemplate;

	private final TourApiResponseParser tourApiResponseParser;

	@Getter
	private final String emptyResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";

	public List<Tour> fetchTourListSinceStartDate(String startDate, Language language) {

		ResponseEntity<String> response = fetchTourMetaData(language, startDate);
		TourMetaData tourMetaData = tourApiResponseParser.mapToTotalPage(response.getBody(), language);
		int totalPage = tourMetaData.totalPage();
		int totalCount = tourMetaData.totalCount();

		List<TourResponse> allTourResponse = new ArrayList<>();
		int pageNo = 1;
		while (pageNo <= totalPage) {
			List<TourResponse> tourResponse = fetchApiData(ApiType.SEARCH,
					new ApiParam(language, 50, pageNo, startDate), TourResponse.class);
			allTourResponse.addAll(tourResponse);
			pageNo += 1;
		}

		log.info("/searchFestival1 언어={} 총 응답 데이터={}개 ", language.getKrName(), totalCount);
		return mapList(allTourResponse, dto -> Tour.createTour(dto, language));
	}

	public TourApiResponse fetchTourRelatedEntities(TourFilterResponse tourFilterResponse, Language language) {
		List<NewTourDto> newToursEntity = fetchRelatedEntity(tourFilterResponse.newTours(), language, "새로운 투어");
		List<NewTourDto> updatedToursEntity = fetchRelatedEntity(tourFilterResponse.updatedTours(), language,
				"업데이트 투어");
		return new TourApiResponse(newToursEntity, updatedToursEntity);
	}

	public List<NewTourDto> fetchRelatedEntity(List<Tour> tours, Language language, String actionType) {

		List<NewTourDto> newTourDtoList = new ArrayList<>();
		int detailCount = 0;
		int repeatCount = 0;
		int imageCount = 0;

		for (Tour tour : tours) {
			ApiParam apiParam = new ApiParam(language, 10, 1, tour.getContentId());

			List<TourDetailResponse> tourDetailResponses = fetchApiData(ApiType.DETAIL, apiParam,
					TourDetailResponse.class);
			List<TourDetail> tourDetails = mapList(tourDetailResponses, dto -> TourDetail.createTourDetail(dto, tour));
			detailCount += tourDetails.size();

			List<TourRepeatResponse> tourRepeatResponses = fetchApiData(ApiType.REPEAT, apiParam,
					TourRepeatResponse.class);
			List<TourRepeat> tourRepeatList = mapList(tourRepeatResponses,
					dto -> TourRepeat.createTourRepeat(dto, tour));
			repeatCount += tourRepeatList.size();

			List<TourImageResponse> tourImageResponses = fetchApiData(ApiType.IMAGE, apiParam, TourImageResponse.class);
			List<TourImage> tourImages = mapList(tourImageResponses, dto -> TourImage.createTourImage(dto, tour));
			imageCount += tourImages.size();

			newTourDtoList.add(new NewTourDto(tour, tourDetails, tourRepeatList, tourImages));
		}
		log.info("언어={} {} Tour={} Detail={} Repeat={} Image={} 조회 완료", language.getKrName(), actionType, tours.size(),
				detailCount, repeatCount, imageCount);

		return newTourDtoList;
	}

	private ResponseEntity<String> fetchTourMetaData(Language language, String startDate) {
		Map<String, String> queryParams = apiResolver.resolveQueryParams(ApiType.SEARCH, 1, 1, startDate);
		String url = apiResolver.resolveEndPoint(language, ApiType.SEARCH, queryParams);
		return callApi(url);
	}

	private <T> List<T> fetchApiData(ApiType apiType, ApiParam apiParam, Class<T> responseType) {
		Map<String, String> queryParams = apiResolver.resolveQueryParams(apiType, apiParam.numberOfRow(),
				apiParam.pageNo(), apiParam.additionalParam());
		String url = apiResolver.resolveEndPoint(apiParam.language(), apiType, queryParams);
		ResponseEntity<String> response = callApi(url);

		return switch (apiType) {
			case SEARCH -> (List<T>) tourApiResponseParser.mapToTourCreateList(response.getBody());
			case IMAGE -> (List<T>) tourApiResponseParser.mapToTourImageCreateList(response.getBody());
			case REPEAT -> (List<T>) tourApiResponseParser.mapToTourRepeatCreateList(response.getBody());
			case DETAIL -> (List<T>) tourApiResponseParser.mapToTourDetailCreate(response.getBody());
			default -> throw new IllegalArgumentException("지원하지 않는 ApiType: " + apiType);
		};
	}

	/**
	 * 관광공사 API 가 불안정하여 업데이트 과정에서 에러가 종종 발생함 때문에, 한번의 문제로 전체 작업을 Shut down 비효율적이라고 판단하여
	 * 예외가 발생했더라도 정상흐름으로 변환하여 작업을 이어나가기로 결정. 또한, 해당 데이터가 다음 작업에서 새롭게 추가될 가능성도 있기 때문에 이 로직이
	 * 나쁘지 않다고 판단.
	 */
	public ResponseEntity<String> callApi(String url) {
		try {
			return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
		}
		catch (ResourceAccessException e) {
			log.warn("url = {} API 요청 실패 ", url, e);
			return new ResponseEntity<>(emptyResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 기본
																							// JSON
																							// 반환
		}
	}

}
