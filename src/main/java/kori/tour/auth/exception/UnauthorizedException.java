package kori.tour.auth.exception;

public class UnauthorizedException extends AuthException {

	public static final String MESSAGE = "권한이 없는 사용자 입니다";

	public UnauthorizedException(Throwable cause) {
		super(MESSAGE, cause);
	}

}
