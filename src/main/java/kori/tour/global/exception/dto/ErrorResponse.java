package kori.tour.global.exception.dto;

import kori.tour.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorMessage;
    private final int errorCode;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorMessage = errorCode.getMessage();
        this.errorCode = errorCode.getHttpStatus() == null ? 500 : errorCode.getHttpStatus().value();
    }

}
