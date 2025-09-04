package kori.tour.global.exception.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import kori.tour.global.exception.NoPermissionException;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.global.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * 로그 결과: "[NotFoundException] 투어를 찾을 수 없습니다."
     * 조금 더 상세하게 짠다면, {pk} 값을 로그로 넘겨줄 수 있는데,, 흠
     * 내가 이 값을 확인한다고 해도 내가 할 수 있는게 없음.
     * 즉, 더 꼼꼼하게 로그를 남길 수 있는데, 그렇다고해서 서비스 운영에 크게 도움 될 것 같지는 않다고 판단
     * */

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> NotFoundExceptionHandler(NotFoundException exception) {
        log.warn("[NotFoundException] {}", exception.getErrorCode().getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(exception.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> NoPermissionExceptionHandler(NoPermissionException exception) {
        log.info("[NoPermissionException] {}", exception.getErrorCode().getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(exception.getErrorCode()));
    }

    /**
     * 추가적으로, 비즈니스 로직 중에서 발생하는 예외를 어떻게 처리할 것 인가 !
     * 그걸 조금 생각해봐야할듯?
     * */

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        log.warn("[MethodArgumentNotValidException] {}", exception.getMessage());
        return ResponseEntity.status(ErrorCode.API_MISSING_PARAM.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.API_MISSING_PARAM));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        log.warn("[MissingServletRequestParameterException] {}", exception.getMessage());
        return ResponseEntity.status(ErrorCode.API_MISSING_PARAM.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.API_MISSING_PARAM));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> ConstraintViolationExceptionHandler(ConstraintViolationException exception) {
        log.warn("[ConstraintViolationException] {}", exception.getMessage());
        return ResponseEntity.status(ErrorCode.API_INVALID_PARAM.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.API_INVALID_PARAM));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> HttpMediaTypeNotAcceptableExceptionHandler(HttpMediaTypeNotAcceptableException exception) {
        log.warn("[HttpMediaTypeNotAcceptableException] {}", exception.getMessage());
        return ResponseEntity.status(ErrorCode.API_NOT_ACCEPTABLE.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.API_NOT_ACCEPTABLE));
    }

}
