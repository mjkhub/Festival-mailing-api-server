package kori.tour.common.exception.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	HTTP_BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"), HTTP_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
	HTTP_FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"), HTTP_NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
	HTTP_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

	private final HttpStatus httpStatus;

	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

}
