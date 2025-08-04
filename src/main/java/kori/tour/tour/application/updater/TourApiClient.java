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
public class TourApiClient { // API 통신 담당

	private final ApiResolver apiResolver;

	private final RestTemplate restTemplate;

	private final TourApiResponseParser tourApiResponseParser;

	@Getter
	private final String emptyResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";

	/**
	 * Retrieves a list of tours starting from the specified date and language.
	 *
	 * Fetches tour metadata to determine the total number of pages, iterates through all pages to collect tour data, and converts the results into domain `Tour` objects.
	 *
	 * @param startDate the start date from which to retrieve tours (inclusive)
	 * @param language the language in which to fetch tour data
	 * @return a list of tours starting from the given date in the specified language
	 */
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

	/**
	 * Retrieves related entities for new and updated tours based on the provided filter response and language.
	 *
	 * For each group (new and updated tours), fetches detailed information, repeat schedules, and images,
	 * then aggregates the results into a {@link TourApiResponse}.
	 *
	 * @param tourFilterResponse the filter response containing lists of new and updated tours
	 * @param language the language to use for API requests
	 * @return a {@link TourApiResponse} containing related entities for both new and updated tours
	 */
	public TourApiResponse fetchTourRelatedEntities(TourFilterResponse tourFilterResponse, Language language) {
		List<NewTourDto> newToursEntity = fetchRelatedEntity(tourFilterResponse.newTours(), language, "새로운 투어");
		List<NewTourDto> updatedToursEntity = fetchRelatedEntity(tourFilterResponse.updatedTours(), language,
				"업데이트 투어");
		return new TourApiResponse(newToursEntity, updatedToursEntity);
	}

	/**
	 * Fetches and aggregates detailed information, repeat schedules, and images for each tour in the provided list.
	 *
	 * For each tour, retrieves detail, repeat, and image data from the external API, maps the responses to domain entities,
	 * and combines them into a {@code NewTourDto}. Returns a list of these DTOs, each representing a tour and its related entities.
	 *
	 * @param tours      the list of tours for which to fetch related entities
	 * @param language   the language to use for API requests
	 * @param actionType a label indicating the context or type of action (e.g., "new" or "update")
	 * @return a list of {@code NewTourDto} objects, each containing a tour and its associated details, repeats, and images
	 */
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

	/**
	 * Retrieves tour metadata from the external API for the specified language and start date.
	 *
	 * Constructs a search API request with pagination set to the first page and a single result, then performs the HTTP request and returns the raw response.
	 *
	 * @param language  the language for the API request
	 * @param startDate the start date to filter tours
	 * @return the raw response entity containing tour metadata
	 */
	private ResponseEntity<String> fetchTourMetaData(Language language, String startDate) {
		Map<String, String> queryParams = apiResolver.resolveQueryParams(ApiType.SEARCH, 1, 1, startDate);
		String url = apiResolver.resolveEndPoint(language, ApiType.SEARCH, queryParams);
		return requestApi(url);
	}

	/**
	 * Fetches and parses data from the external tour API for the specified API type and parameters.
	 *
	 * Depending on the provided {@code apiType}, this method retrieves the corresponding data (such as tours, images, repeats, or details)
	 * and parses the API response into a list of domain DTOs.
	 *
	 * @param apiType      the type of API endpoint to query (e.g., SEARCH, IMAGE, REPEAT, DETAIL)
	 * @param apiParam     the parameters for the API request, including pagination and language
	 * @param responseType the expected class type of the response DTOs
	 * @return a list of parsed DTOs corresponding to the API type
	 * @throws IllegalArgumentException if the provided {@code apiType} is not supported
	 */
	private <T> List<T> fetchApiData(ApiType apiType, ApiParam apiParam, Class<T> responseType) {
		Map<String, String> queryParams = apiResolver.resolveQueryParams(apiType, apiParam.numberOfRow(),
				apiParam.pageNo(), apiParam.additionalParam());
		String url = apiResolver.resolveEndPoint(apiParam.language(), apiType, queryParams);
		ResponseEntity<String> response = requestApi(url);

		return switch (apiType) {
			case SEARCH -> (List<T>) tourApiResponseParser.mapToTourCreateList(response.getBody());
			case IMAGE -> (List<T>) tourApiResponseParser.mapToTourImageCreateList(response.getBody());
			case REPEAT -> (List<T>) tourApiResponseParser.mapToTourRepeatCreateList(response.getBody());
			case DETAIL -> (List<T>) tourApiResponseParser.mapToTourDetailCreate(response.getBody());
			default -> throw new IllegalArgumentException("지원하지 않는 ApiType: " + apiType);
		};
	}

	/**
	 * Executes an HTTP GET request to the specified URL and returns the response.
	 *
	 * If a network access error occurs, returns a predefined empty JSON response with HTTP 200 OK status.
	 *
	 * @param url the URL to send the GET request to
	 * @return the HTTP response entity containing the response body as a string
	 */
	public ResponseEntity<String> requestApi(String url) {

		try {
			return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
		}
		catch (ResourceAccessException e) {
			log.warn("url = {} API 요청 실패 ", url, e);
			return new ResponseEntity<>(emptyResponse, HttpStatus.OK); // 기본 JSON 반환
		}
	}

}
