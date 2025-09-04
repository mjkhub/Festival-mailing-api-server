package kori.tour.global.exception;

import kori.tour.global.exception.code.ErrorCode;

public class NoPermissionException extends BusinessException {

    public NoPermissionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
