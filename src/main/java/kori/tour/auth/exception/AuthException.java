package kori.tour.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

}
