package kori.tour.tour.application.updater;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import lombok.Getter;

@Getter
public enum ApiType {

	// 서비스 api path job Path
	SEARCH("searchFestival1", (params, eventStartDate) -> { // String 을 하나 정도 더 받을 수 있다.
															// eventEndDate
		Map<String, String> queryParams = params.mapToParamMap();
		queryParams.put("eventStartDate", eventStartDate);
		return queryParams; // 이게 요구 사항이 완벽 하게 나온게 아니라
	}), DETAIL("detailIntro1", (params, contentId) -> {
		Map<String, String> queryParams = params.mapToParamMap();
		queryParams.put("contentId", contentId);
		queryParams.put("contentTypeId", "15");
		return queryParams;
	}), REPEAT("detailInfo1", (params, contentId) -> {
		Map<String, String> queryParams = params.mapToParamMap();
		queryParams.put("contentId", contentId);
		queryParams.put("contentTypeId", "15");
		return queryParams;
	}), IMAGE("detailImage1", (params, contentId) -> {
		Map<String, String> queryParams = params.mapToParamMap();
		queryParams.put("contentId", contentId);
		return queryParams;
	});

	private final String path;
	private final BiFunction<BasicApiParams, String, Map<String, String>> queryParamResolver;
	private final String FESTIVAL_CONTENT_TYPE_ID = "15"; // ContentTypeID 15: 한국의 축제공연행사 85:다국어 축제공연행사 파라미터값

	ApiType(String path, BiFunction<BasicApiParams, String, Map<String, String>> queryParamResolver) {
		this.path = path;
		this.queryParamResolver = queryParamResolver;
	}

	public static BasicApiParams createBasicApiParams(int numberOfRows, int pageNo) {
		return new BasicApiParams(numberOfRows, pageNo);
	}

	@Getter
	public static class BasicApiParams {

		private final String numberOfRows;

		private final String pageNo;

		private BasicApiParams(int numberOfRows, int pageNo) {
			this.numberOfRows = String.valueOf(numberOfRows);
			this.pageNo = String.valueOf(pageNo);
		}

		private Map<String, String> mapToParamMap() {
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("numOfRows", numberOfRows);
			queryParams.put("pageNo", pageNo);
			return queryParams;
		}

	}

}
