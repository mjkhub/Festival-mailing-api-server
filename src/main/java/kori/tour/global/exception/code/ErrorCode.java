package kori.tour.global.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	/**
	 * API 예외에 사용할 에러코드 정의
	 */

	AREA_CODE_FILE(HttpStatus.BAD_REQUEST, "지역 코드 파일을 읽는 데 실패했습니다"),
	AREA_NOT_FOUND(HttpStatus.BAD_REQUEST, "지역 코드 혹은 시군구 코드를 찾을 수 없습니다"),


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
