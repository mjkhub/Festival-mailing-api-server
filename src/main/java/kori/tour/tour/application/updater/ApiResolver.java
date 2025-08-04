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

	/**
	 * Constructs the full API endpoint URL for a given language and API type, optionally including query parameters.
	 *
	 * @param language the language to use in the API path segment
	 * @param apiType the type of API endpoint to target
	 * @param queryParams optional map of query parameters to append to the URL; may be null
	 * @return the complete API endpoint URL as a string
	 */
	public String resolveEndPoint(Language language, ApiType apiType, Map<String, String> queryParams) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
			.scheme("https")
			.host("apis.data.go.kr")
			.path("/B551011/" + language.getPath() + "/" + apiType.getPath());

		if (queryParams != null)
			queryParams.forEach(uriBuilder::queryParam);

		return uriBuilder.build().toUriString();
	}

	/**
	 * Generates a map of query parameters for an API request based on the specified API type, pagination settings, and an additional parameter.
	 *
	 * Combines basic pagination parameters with API type-specific parameters and appends standard required query parameters.
	 *
	 * @param apiType the type of API for which to generate query parameters
	 * @param numberOfRows the number of rows to request per page
	 * @param pageNo the page number to request
	 * @param additionalParam an additional parameter used by the API type's query parameter resolver
	 * @return a map containing all query parameters for the API request
	 */
	public Map<String, String> resolveQueryParams(ApiType apiType, int numberOfRows, int pageNo,
			String additionalParam) {
		ApiType.BasicApiParams basicApiParams = ApiType.createBasicApiParams(numberOfRows, pageNo);
		Map<String, String> queryParam = apiType.getQueryParamResolver().apply(basicApiParams, additionalParam);
		addBasicQueryParam(queryParam);
		return queryParam;
	}

	/**
	 * Adds standard API query parameters to the provided map, including the service key, mobile OS, app identifier, and response type.
	 *
	 * @param queryParam the map to which standard query parameters will be added
	 */
	private void addBasicQueryParam(Map<String, String> queryParam) {
		queryParam.put("serviceKey", serviceKey);
		queryParam.put("MobileOS", "ETC");
		queryParam.put("MobileApp", "AppTest");
		queryParam.put("_type", "json");
	}

}
