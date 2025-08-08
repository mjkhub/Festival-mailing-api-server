package kori.tour.global.exception;


import kori.tour.global.exception.code.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
