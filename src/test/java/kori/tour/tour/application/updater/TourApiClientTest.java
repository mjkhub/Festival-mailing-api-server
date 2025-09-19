package kori.tour.tour.application.updater;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.application.updater.dto.TourApiResponse;
import kori.tour.tour.application.updater.dto.TourFilterResponse;
import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourType;


@SpringBootTest
class TourApiClientTest {

    @Autowired
    TourApiClient tourApiClient;

    @Autowired
    RestTemplate restTemplate;

    MockRestServiceServer server;

    final String startDate = "2025-08-01";
    final Language lang = Language.KOREAN;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("메타 데이터 → 페이지 순회 → Tour 리스트")
    void fetchTourListSinceStartDate_success() {
        // 메타 데이터 응답 (총 2페이지: ceil(75/50)=2)
        String metaDataResponse = "{\"response\":{\"body\":{\"items\":{},\"numOfRows\":50,\"pageNo\":1,\"totalCount\":75}}}";

        server.expect(times(1), requestTo(containsString("/searchFestival2")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("pageNo", "1"))
                .andExpect(queryParam("numOfRows", "1"))
                .andExpect(queryParam("eventStartDate", startDate))
                .andRespond(withSuccess(metaDataResponse, MediaType.APPLICATION_JSON));

        // 페이지 1 (numOfRows=50, pageNo=1, eventStartDate=startDate)
        String pageOneResponse = "{ \"response\": { \"body\": { \"items\": " +
                "{ \"item\": [ { \"contentid\":\"1001\",\"title\":\"축제A\",\"contenttypeid\":\"15\" }, " +
                "{ \"contentid\":\"1002\",\"title\":\"축제B\",\"contenttypeid\":\"15\" } ] } } } }";

        server.expect(times(1), requestTo(containsString("/searchFestival2")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("pageNo", "1"))
                .andExpect(queryParam("numOfRows", "50"))
                .andExpect(queryParam("eventStartDate", startDate))
                .andRespond(withSuccess(pageOneResponse, MediaType.APPLICATION_JSON));

        // 페이지 2
        String pageTwoResponse = "{ \"response\": { \"body\": { \"items\": " +
                "{ \"item\": [ { \"contentid\":\"1003\",\"title\":\"축제C\",\"contenttypeid\":\"15\" } ] } } } }";

        server.expect(times(1), requestTo(containsString("/searchFestival2")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("pageNo", "2"))
                .andExpect(queryParam("numOfRows", "50"))
                .andExpect(queryParam("eventStartDate", startDate))
                .andRespond(withSuccess(pageTwoResponse, MediaType.APPLICATION_JSON));

        // when
        List<Tour> tours = tourApiClient.fetchTourListSinceStartDate(startDate, lang);

        // then
        server.verify();
        assertThat(tours).hasSize(3);
        assertThat(tours).extracting(Tour::getContentId)
                .containsExactlyInAnyOrder("1001", "1002", "1003");
    }

    @Test
    @DisplayName("투어 필터 응답(신규/업데이트) -> 관련 엔티티 조회 -> Tour API 응답")
    void fetchTourRelatedEntities_success() {
        // given
        Tour newTour = Tour.builder().contentId("3001").contentTypeId(TourType.FESTIVAL).build();
        Tour updatedTour = Tour.builder().contentId("3002").contentTypeId(TourType.FESTIVAL).build();
        TourFilterResponse tourFilterResponse = new TourFilterResponse(List.of(newTour), List.of(updatedTour));

        // Mock API responses for newTour (3001)
        String newTourDetailResponse = """
            { "response": { "body": { "items": { "item": [ { "eventhomepage": "<p>홈페이지1</p>", "playtime": "상시", "usetimefestival": "무료" } ] } } } }
            """;
        String newTourRepeatResponse = """
            { "response": { "body": { "items": { "item": [ { "serialnum": "1", "infoname": "주최", "infotext": "주최자1" } ] } } } }
            """;
        String newTourImageResponse = """
            { "response": { "body": { "items": { "item": [ { "serialnum": "img1", "originimgurl": "http://image.com/new.jpg", "smallimageurl": "http://image.com/new_small.jpg", "imgname": "이미지1" } ] } } } }
            """;

        // Mock API responses for updatedTour (3002)
        String updatedTourDetailResponse = """
            { "response": { "body": { "items": { "item": [ { "eventhomepage": "<p>홈페이지2</p>", "playtime": "10:00-18:00", "usetimefestival": "성인 10,000원" } ] } } } }
            """;
        String updatedTourRepeatResponse = """
            { "response": { "body": { "items": { "item": [ { "serialnum": "2", "infoname": "주관", "infotext": "주관사2" } ] } } } }
            """;
        String updatedTourImageResponse = """
            { "response": { "body": { "items": { "item": [ { "serialnum": "img2", "originimgurl": "http://image.com/updated.jpg", "smallimageurl": "http://image.com/updated_small.jpg", "imgname": "이미지2" } ] } } } }
            """;


        // Expect calls for newTour
        expectApiCallForRelatedEntities("/detailIntro2", "3001", newTourDetailResponse);
        expectApiCallForRelatedEntities("/detailInfo2", "3001", newTourRepeatResponse);
        expectApiCallForRelatedEntities("/detailImage2", "3001", newTourImageResponse);

        // Expect calls for updatedTour
        expectApiCallForRelatedEntities("/detailIntro2", "3002", updatedTourDetailResponse);
        expectApiCallForRelatedEntities("/detailInfo2", "3002", updatedTourRepeatResponse);
        expectApiCallForRelatedEntities("/detailImage2", "3002", updatedTourImageResponse);

        // when
        TourApiResponse response = tourApiClient.fetchTourRelatedEntities(tourFilterResponse, lang);

        // then
        server.verify();

        // Assertions for new tour
        assertThat(response.newToursEntity()).hasSize(1);
        NewTourDto newTourDto = response.newToursEntity().get(0);
        assertThat(newTourDto.getTour().getContentId()).isEqualTo("3001");
        assertThat(newTourDto.getDetailInfo().getEventHomepage()).isEqualTo("<p>홈페이지1</p>");
        assertThat(newTourDto.getTourRepeatList().get(0).getInfoName()).isEqualTo("주최");
        assertThat(newTourDto.getTourImageList().get(0).getOriginImageUrl()).isEqualTo("http://image.com/new.jpg");

        // Assertions for updated tour
        assertThat(response.updatedToursEntity()).hasSize(1);
        NewTourDto updatedTourDto = response.updatedToursEntity().get(0);
        assertThat(updatedTourDto.getTour().getContentId()).isEqualTo("3002");
        assertThat(updatedTourDto.getDetailInfo().getEventHomepage()).isEqualTo("<p>홈페이지2</p>");
        assertThat(updatedTourDto.getTourRepeatList().get(0).getInfoName()).isEqualTo("주관");
        assertThat(updatedTourDto.getTourImageList().get(0).getOriginImageUrl()).isEqualTo("http://image.com/updated.jpg");

    }

    private void expectApiCallForRelatedEntities(String apiPath, String contentId, String responseBody) {
        server.expect(times(1), requestTo(containsString(apiPath)))
              .andExpect(method(HttpMethod.GET))
              .andExpect(queryParam("contentId", contentId))
              .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }
}
