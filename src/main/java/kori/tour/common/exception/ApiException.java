package kori.tour.common.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
