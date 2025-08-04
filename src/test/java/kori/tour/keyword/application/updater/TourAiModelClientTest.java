package kori.tour.keyword.application.updater;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TourAiModelClientTest {

	@Autowired
	TourAiModelClient tourAiModelClient;

	@Autowired
	PromptBuilder promptBuilder;

	@Test
	@DisplayName("요청이 정상적으로 처리되는지 확인용 테스트")
	void test() {
		// Given
		String document = "{\n" + "  \"festivalName\": \"서울빛초롱축제\",\n" + "  \"programs\": \"등불 전시, 문화 공연, 체험 활동\",\n"
				+ "  \"eventLocation\": \"청계천, 서울, 대한민국\",\n" + "  \"eventHours\": \"18:00 - 23:00\",\n"
				+ "  \"duration\": \"11월 3일 - 11월 19일\",\n" + "  \"admissionFee\": \"원데이 패스 3만원, 투데이패스 5만원\",\n"
				+ "  \"ageLimit\": \"전 연령 가능\",\n" + "  \"festivalDetails\": {\n" + "    \"theme\": \"전통과 현대의 등불\",\n"
				+ "    \"organizer\": \"서울관광재단\",\n" + "    \"mainAttractions\": \"대형 등불 전시, 먹거리 부스, 빛 공연\"\n" + "  }\n"
				+ "}";

		// When
		System.out.println(tourAiModelClient.call(promptBuilder.buildKeywordPrompt(document)));
	}

}
