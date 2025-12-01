package kori.tour.keyword.application.updater.parser;

import kori.tour.global.exception.code.ErrorCode;

public class AiApiException extends RuntimeException {

	public AiApiException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
	}

	public AiApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}

}
