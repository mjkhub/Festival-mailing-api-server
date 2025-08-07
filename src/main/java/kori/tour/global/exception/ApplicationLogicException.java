package kori.tour.global.exception;


import kori.tour.global.exception.code.ErrorCode;

public class ApplicationLogicException extends BusinessException {
    public ApplicationLogicException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApplicationLogicException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
