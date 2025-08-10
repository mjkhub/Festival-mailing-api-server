package kori.tour.tour.application.updater;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import kori.tour.tour.application.port.out.TourCrudPort;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.application.updater.dto.TourApiResponse;
import kori.tour.tour.application.updater.dto.TourFilterResponse;
import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.Tour;

@SpringBootTest
class TourUpdateServiceTest {

    @Autowired
    TourUpdateService tourUpdateService;

    @MockitoBean
    TourCrudPort tourCrudPort;

    @Test
    @DisplayName("새로운 투어, 업데이트 투어 분리 로직")
    void givenNewAndExistingTours_whenSeparateUpdateTourNewTour_thenReturnsSeparatedTours() {
        // given
        Language language = Language.KOREAN;

        Tour existingTour = Tour.builder().id(1L).contentId("123").language(language).build();
        Tour newTour = Tour.builder().contentId("456").language(language).build();

        List<Tour> tours = List.of(existingTour, newTour);

        when(tourCrudPort.isExisting(any(Tour.class))).thenAnswer(invocation -> {
            Tour tour = invocation.getArgument(0);
            return "123".equals(tour.getContentId());
        });

        when(tourCrudPort.isUpdated(any(Tour.class))).thenReturn(true);

        // when
        TourFilterResponse response = tourUpdateService.separateUpdateTourNewTour(tours, language);

        // then
        assertThat(response.newTours()).containsExactly(newTour);
        assertThat(response.updatedTours()).containsExactly(existingTour);
    }

    @Test
    @DisplayName("업데이트 대상 투어 목록을 받으면 해당 투어를 삭제한다")
    void givenUpdatedTours_whenUpdateTours_thenDeletesTours() {
        // given
        Language language = Language.KOREAN;

        Tour updatedTour1 = Tour.builder().id(1L).contentId("101").build();
        Tour updatedTour2 = Tour.builder().id(2L).contentId("102").build();

        NewTourDto updatedDto1 = new NewTourDto(updatedTour1, List.of(), List.of(), List.of());
        NewTourDto updatedDto2 = new NewTourDto(updatedTour2, List.of(), List.of(), List.of());

        List<NewTourDto> updatedDtos = List.of(updatedDto1, updatedDto2);
        TourApiResponse tourApiResponse = new TourApiResponse(List.of(), updatedDtos);

        List<String> contentIds = List.of("101", "102");
        List<Long> tourPks = List.of(1L, 2L);

        when(tourCrudPort.findIdListByContentIdList(contentIds)).thenReturn(tourPks);

        // when
        TourApiResponse response = tourUpdateService.updateTours(tourApiResponse, language);

        // then
        verify(tourCrudPort).findIdListByContentIdList(contentIds);
        verify(tourCrudPort).deleteToursAndRelatedEntities(tourPks);

        assertThat(response).isEqualTo(tourApiResponse);
    }

    @Test
    @DisplayName("새로운 투어 목록을 저장하고 응답을 반환한다")
    void givenNewTours_whenSaveNewTours_thenSavesToursAndReturnsResponse() {
        // given
        Language language = Language.KOREAN;

        NewTourDto newTourDto1 = new NewTourDto(Tour.builder().contentId("789").build(), List.of(), List.of(), List.of());
        NewTourDto newTourDto2 = new NewTourDto(Tour.builder().contentId("101").build(), List.of(), List.of(), List.of());
        List<NewTourDto> newTours = List.of(newTourDto1, newTourDto2);

        TourApiResponse tourApiResponse = new TourApiResponse(newTours, List.of());

        // when
        TourApiResponse response = tourUpdateService.saveNewTours(tourApiResponse, language);

        // then
        verify(tourCrudPort, times(2)).saveTourListBulk(newTours);
        assertThat(response).isEqualTo(tourApiResponse);
    }

    @Test
    @DisplayName("TourApiResponse의 투어 개수를 AtomicInteger에 더한다")
    void givenTourApiResponse_whenSumTourEntity_thenAccumulatesCorrectly() {
        // given
        NewTourDto newTourDto = new NewTourDto(Tour.builder().build(), List.of(), List.of(), List.of());
        NewTourDto updatedTourDto = new NewTourDto(Tour.builder().build(), List.of(), List.of(), List.of());

        // 새로운 투어 3개, 업데이트된 투어 2개
        TourApiResponse tourApiResponse = new TourApiResponse(
                List.of(newTourDto, newTourDto, newTourDto),
                List.of(updatedTourDto, updatedTourDto)
        );

        AtomicInteger count = new AtomicInteger(10); // 초기값 10
        int expected = 10 + 3 + 2;

        // when
        tourUpdateService.sumTourEntity(count, tourApiResponse);

        // then
        assertThat(count.get()).isEqualTo(expected);
    }




}
