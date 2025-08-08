package kori.tour.global.exception;


import kori.tour.global.exception.code.ErrorCode;


// Todo Spring AOP 를 활용해서 예외를 공통처리
public class AsyncProcessingException extends BusinessException {
    public AsyncProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AsyncProcessingException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
