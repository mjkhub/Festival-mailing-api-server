package kori.tour.auth.exception;

public class UnauthorizedException extends AuthException {

    public static final String MESSAGE = "권한이 없는 사용자 입니다";

    /**
     * Constructs an UnauthorizedException with a predefined message indicating lack of user permission and the specified cause.
     *
     * @param cause the underlying cause of the exception
     */
    public UnauthorizedException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
