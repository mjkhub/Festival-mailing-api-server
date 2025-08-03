package kori.tour.tour.application.service.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import kori.tour.tour.application.updater.parser.TourTimeUtil;

class TourTimeUtilTest {


	@Test
	void parseLocalDate() {
		// given
		String date = "20240612";
		LocalDate answer = LocalDate.of(2024, 6, 12);

		// When
		LocalDate parsedLocalDate = TourTimeUtil.parseLocalDate(date);

		// Then
		assertThat(parsedLocalDate).isNotNull();
		assertThat(parsedLocalDate.equals(answer)).isTrue();
	}

	@Test
	void parseToLocalDateTime() {
		// given
		String datetime = "20240612153045";
		LocalDateTime expectedDateTime = LocalDateTime.of(2024, 6, 12, 15, 30, 45);

		// when
		LocalDateTime parsedLocalDateTime = TourTimeUtil.parseToLocalDateTime(datetime);

		// then
		assertThat(parsedLocalDateTime).isNotNull();
		assertThat(parsedLocalDateTime).isEqualTo(expectedDateTime);
	}

}
