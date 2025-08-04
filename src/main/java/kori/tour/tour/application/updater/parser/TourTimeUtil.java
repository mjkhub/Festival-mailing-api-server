package kori.tour.tour.application.updater.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TourTimeUtil {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	/**
	 * Parses a string in "yyyyMMdd" format into a {@link LocalDate}.
	 *
	 * @param date the date string to parse, expected in "yyyyMMdd" format
	 * @return the parsed {@code LocalDate}, or {@code null} if the input is {@code null} or empty
	 */
	public static LocalDate parseLocalDate(String date) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		return LocalDate.parse(date, dateFormatter);
	}

	/**
	 * Parses a string in the format "yyyyMMddHHmmss" into a {@link LocalDateTime} object.
	 *
	 * @param datetime the date-time string to parse, or {@code null}
	 * @return the parsed {@link LocalDateTime}, or {@code null} if the input is {@code null} or empty
	 */
	public static LocalDateTime parseToLocalDateTime(String datetime) {
		if (datetime == null || datetime.isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(datetime, dateTimeFormatter);
	}

}
