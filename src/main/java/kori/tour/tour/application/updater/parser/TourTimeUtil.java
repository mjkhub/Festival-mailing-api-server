package kori.tour.tour.application.updater.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TourTimeUtil {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static LocalDate parseLocalDate(String date) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		return LocalDate.parse(date, dateFormatter);
	}

	public static LocalDateTime parseToLocalDateTime(String datetime) {
		if (datetime == null || datetime.isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(datetime, dateTimeFormatter);
	}

}
