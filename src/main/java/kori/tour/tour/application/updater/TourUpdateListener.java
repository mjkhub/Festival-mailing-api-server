package kori.tour.tour.application.updater;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import kori.tour.tour.application.port.out.KeywordExtractingEventPort;
import kori.tour.tour.application.updater.dto.TourApiResponse;
import kori.tour.tour.application.updater.dto.TourFilterResponse;
import kori.tour.tour.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourUpdateListener {

	private final TourApiClient tourApiClient;

	private final TourUpdateService tourUpdateService;

	private final KeywordExtractingEventPort keywordExtractingEventPort;

	private final ThreadPoolTaskExecutor tourUpdaterThreadTaskExecutor;

	@Scheduled(cron = "0 0 9-18 * * *", zone = "Asia/Seoul")
	public void updateTour() {
		log.info("######################## 축제 정보 언어별 업데이트 시작 ######################## ");
		long startTime = System.currentTimeMillis();
		String startDate = getStartDate();
		AtomicInteger count = new AtomicInteger();
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (Language language : Language.values()) {
			CompletableFuture<Void> future = CompletableFuture
				.supplyAsync(() -> tourApiClient.fetchTourListSinceStartDate(startDate, language),
						tourUpdaterThreadTaskExecutor)
				.thenApply((tours) -> tourUpdateService.separateUpdateTourNewTour(tours, language))
				.thenApply((filterResponse) -> tourApiClient.fetchTourRelatedEntities(filterResponse, language))
				.thenApply((apiResponse) -> tourUpdateService.updateTours(apiResponse, language))
				.thenApply((apiResponse) -> tourUpdateService.saveNewTours(apiResponse, language))
				// TourApiResponse tourApiResponse 이 타입이 전달 흠 그래서 지금 약간 불편해
				.thenAccept((apiResponse) -> {
					tourUpdateService.sumTourEntity(count, apiResponse);
					keywordExtractingEventPort.sendKeywordExtractingEvent(apiResponse.newToursEntity());
					keywordExtractingEventPort.sendKeywordExtractingEvent(apiResponse.updatedToursEntity());
				});
			futures.add(future);
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		long endTime = System.currentTimeMillis();
		log.info("멀티스레드 축제 정보 {}개 업데이트 완료 총 소요시간 = {}ms", count, endTime - startTime);
	}

	public void singleThread() { // 로직이 더욱 명시적으로 드러나서 지우지는 않음
		log.info("싱글스레드 시작");
		String startDate = getStartDate();
		for (Language language : Language.values()) {
			List<Tour> tours = tourApiClient.fetchTourListSinceStartDate(startDate, language);
			TourFilterResponse filterResponse = tourUpdateService.separateUpdateTourNewTour(tours, language);
			TourApiResponse apiResponse = tourApiClient.fetchTourRelatedEntities(filterResponse, language);
			apiResponse = tourUpdateService.updateTours(apiResponse, language);
			apiResponse = tourUpdateService.saveNewTours(apiResponse, language);
		}
		log.info("싱글 스레드 끗");
	}

	private String getStartDate() {
		return LocalDate.now(ZoneId.of("Asia/Seoul")).minusMonths(6).toString();
	}

}
