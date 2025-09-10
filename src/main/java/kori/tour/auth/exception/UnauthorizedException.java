package kori.tour.auth.exception;

public class UnauthorizedException extends AuthException {

	public static final String MESSAGE = "유효하지 않은 토큰입니다.";

	public UnauthorizedException(Throwable cause) {
		super(MESSAGE, cause);
	}

}
