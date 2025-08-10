package kori.tour.tour.application.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kori.tour.tour.application.updater.TourUpdateListener;

@Disabled
@SpringBootTest
class TourUpdateListenerTest {

	@Autowired
	TourUpdateListener tourUpdateListener;

	@Test
	void 동작하는걸_보기위한_테스트() {
		tourUpdateListener.updateTour();
	}

}
