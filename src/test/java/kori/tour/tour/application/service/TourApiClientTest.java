package kori.tour.tour.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import kori.tour.tour.application.updater.TourApiClient;

@SpringBootTest
class TourApiClientTest {

	@Autowired
	TourApiClient tourApiClient;

	@Test
	@DisplayName("투어 클라이언트 API 요청 실패 경우")
	void test() {
		// given
		String wrongEndpoint = "http://localhost:8081/api/accounts/1";

		// when
		ResponseEntity<String> response = tourApiClient.callApi(wrongEndpoint);

		// then
		Assertions.assertThat(response.getBody()).isEqualTo(tourApiClient.getEmptyResponse());
	}

}
