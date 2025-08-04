package kori.tour.tour.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import kori.tour.tour.application.updater.dto.TourMetaData;
import kori.tour.tour.application.updater.parser.TourApiResponseParser;
import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.dto.TourDetailResponse;
import kori.tour.tour.domain.dto.TourImageResponse;
import kori.tour.tour.domain.dto.TourRepeatResponse;
import kori.tour.tour.domain.dto.TourResponse;

class TourApiResponseParserTest {

	TourApiResponseParser tourApiResponseParser = new TourApiResponseParser();

	@Test
	void 새로운축제헤더_정상응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"numOfRows\": 10, \"pageNo\": 1, \"totalCount\": 43 } } }";

		// When
		TourMetaData tourMetaData = tourApiResponseParser.mapToTotalPage(jsonResponse, Language.KOREAN);

		// Then
		assertThat(tourMetaData.totalCount()).isEqualTo(43);
		assertThat(tourMetaData.totalPage()).isEqualTo(5);
	}

	@Test
	void 새로운축제헤더_XML응답() {
		// Given
		String jsonResponse = "<OpenAPI ServiceResponse><cmmMsgHeader><errMsg>SERVICE ERROR</errMsg><returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg><returnReasonCode>30</returnReasonCode></cmmMsgHeader></OpenAPI ServiceResponse>";

		// When -> it leaves log ~
		TourMetaData tourMetaData = tourApiResponseParser.mapToTotalPage(jsonResponse, Language.KOREAN);

		// Then
		assertThat(tourMetaData.totalCount()).isEqualTo(0);
		assertThat(tourMetaData.totalPage()).isEqualTo(0);
	}

	@Test
	void 새로운축제조회_정상응답() {
		// given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": [ { \"addr1\": \"206 Songpanaru-gil, Songpa-gu, Seoul\", \"addr2\": \"\", \"areacode\": \"1\", \"contentid\": \"3421970\", \"contenttypeid\": \"85\", \"createdtime\": \"20241119155027\", \"eventstartdate\": \"20241025\", \"eventenddate\": \"20250228\", \"firstimage\": \"http://tong.visitkorea.or.kr/cms/resource/75/3387975_image2_1.jpg\", \"firstimage2\": \"http://tong.visitkorea.or.kr/cms/resource/75/3387975_image3_1.jpg\", \"mapx\": \"127.1042611168\", \"mapy\": \"37.5119517366\", \"mlevel\": \"6\", \"modifiedtime\": \"20241121091157\", \"sigungucode\": \"18\", \"tel\": \"+82-2-2147-2110\", \"title\": \"Autumn and Winter of the Lake and the Luminaries (호수의 가을과 겨울,그리고 루미나리에)\" } ] } } } }";

		// when
		List<TourResponse> tourResponses = tourApiResponseParser.mapToTourCreateList(jsonResponse);

		// then -> 값 매핑 정상 동작 ( 테스트 코드가 지저분 -> 값을 확인하는 코드 삭제 )
		assertThat(tourResponses.size()).isEqualTo(1);
	}

	@Test
	void 새로운축제조회_텅빈응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";

		// When
		List<TourResponse> tourResponses = tourApiResponseParser.mapToTourCreateList(jsonResponse);

		// Then
		assertThat(tourResponses).isEmpty();
	}

	@Test
	void 새로운축제조회_XML응답() {
		// Given
		String jsonResponse = "<OpenAPI ServiceResponse><cmmMsgHeader><errMsg>SERVICE ERROR</errMsg><returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg><returnReasonCode>30</returnReasonCode></cmmMsgHeader></OpenAPI ServiceResponse>";

		// When -> it leaves log ~
		List<TourResponse> tourResponses = tourApiResponseParser.mapToTourCreateList(jsonResponse);

		// Then
		assertThat(tourResponses).isEmpty();
	}

	@Test
	void 축제디테일정보조회_정상응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": [ { \"contentid\": \"3421970\", \"contenttypeid\": \"85\", \"agelimit\": \"\", \"bookingplace\": \"\", \"discountinfofestival\": \"\", \"eventenddate\": \"20250228\", \"eventhomepage\": \"\", \"eventplace\": \"Areas of Seokchonhosu Lake\", \"eventstartdate\": \"20241025\", \"placeinfo\": \"\", \"playtime\": \"12:00-20:00 * Light turns off at 22:30\", \"program\": \"\", \"spendtimefestival\": \"\", \"sponsor1\": \"Songpa-gu\", \"sponsor1tel\": \"+82-2-2147-2110\", \"sponsor2\": \"\", \"sponsor2tel\": \"\", \"subevent\": \"\", \"usetimefestival\": \"Free\" } ] } } } }";

		// When
		List<TourDetailResponse> tourDetailCreate = tourApiResponseParser.mapToTourDetailCreate(jsonResponse);

		// Then
		assertThat(tourDetailCreate.isEmpty()).isFalse();
	}

	@Test
	void 축제디테일정보조회_xml응답() {
		// Given
		String jsonResponse = "<OpenAPI ServiceResponse><cmmMsgHeader><errMsg>SERVICE ERROR</errMsg><returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg><returnReasonCode>30</returnReasonCode></cmmMsgHeader></OpenAPI ServiceResponse>";

		// When
		List<TourDetailResponse> tourDetailCreate = tourApiResponseParser.mapToTourDetailCreate(jsonResponse);

		// Then
		assertThat(tourDetailCreate.isEmpty()).isTrue();
	}

	@Test
	void 축제디테일정보조회_텅빈응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";
		// When
		List<TourDetailResponse> tourDetailResponses = tourApiResponseParser.mapToTourDetailCreate(jsonResponse);
		// Then
		assertThat(tourDetailResponses).isEmpty();
	}

	@Test
	void 축제반복정보조회_정상응답() {
		// Given

		// When

		// Then
	}

	@Test
	void 축제반복정보조회_xml응답() {
		// Given
		String jsonResponse = "<OpenAPI ServiceResponse><cmmMsgHeader><errMsg>SERVICE ERROR</errMsg><returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg><returnReasonCode>30</returnReasonCode></cmmMsgHeader></OpenAPI ServiceResponse>";

		// When
		List<TourRepeatResponse> tourRepeatResponses = tourApiResponseParser.mapToTourRepeatCreateList(jsonResponse);

		// Then
		assertThat(tourRepeatResponses).isEmpty();
	}

	@Test
	void 축제반복정보조회_텅빈응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";
		// When
		List<TourRepeatResponse> tourRepeatResponses = tourApiResponseParser.mapToTourRepeatCreateList(jsonResponse);

		// Then
		assertThat(tourRepeatResponses).isEmpty();
	}

	@Test
	void 축제이미지정보조회_정상응답() {
		// Given

		// When

		// Then
	}

	@Test
	void 축제이미지정보조회_xml응답() {
		// Given
		String jsonResponse = "<OpenAPI ServiceResponse><cmmMsgHeader><errMsg>SERVICE ERROR</errMsg><returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg><returnReasonCode>30</returnReasonCode></cmmMsgHeader></OpenAPI ServiceResponse>";

		// When
		List<TourImageResponse> tourImageResponses = tourApiResponseParser.mapToTourImageCreateList(jsonResponse);

		// Then
		assertThat(tourImageResponses).isEmpty();
	}

	@Test
	void 축제이미지정보조회_텅빈응답() {
		// Given
		String jsonResponse = "{ \"response\": { \"body\": { \"items\": { \"item\": \"\" } } } }";
		// When
		List<TourImageResponse> tourImageResponses = tourApiResponseParser.mapToTourImageCreateList(jsonResponse);

		// Then
		assertThat(tourImageResponses).isEmpty();
	}

}
