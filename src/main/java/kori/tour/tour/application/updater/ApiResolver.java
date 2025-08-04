package kori.tour.tour.application.updater;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import kori.tour.tour.domain.Language;

@Component
public class ApiResolver {

	@Value("${tour.api-key}")
	private String serviceKey;

	public String resolveEndPoint(Language language, ApiType apiType, Map<String, String> queryParams) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
			.scheme("https")
			.host("apis.data.go.kr")
			.path("/B551011/" + language.getPath() + "/" + apiType.getPath());

		if (queryParams != null)
			queryParams.forEach(uriBuilder::queryParam);

		return uriBuilder.build().toUriString();
	}

	public Map<String, String> resolveQueryParams(ApiType apiType, int numberOfRows, int pageNo,
			String additionalParam) {
		ApiType.BasicApiParams basicApiParams = ApiType.createBasicApiParams(numberOfRows, pageNo);
		Map<String, String> queryParam = apiType.getQueryParamResolver().apply(basicApiParams, additionalParam);
		addBasicQueryParam(queryParam);
		return queryParam;
	}

	private void addBasicQueryParam(Map<String, String> queryParam) {
		queryParam.put("serviceKey", serviceKey);
		queryParam.put("MobileOS", "ETC");
		queryParam.put("MobileApp", "AppTest");
		queryParam.put("_type", "json");
	}

}
