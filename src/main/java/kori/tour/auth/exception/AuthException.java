package kori.tour.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    /**
     * Constructs a new AuthException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of the exception
     */
    public AuthException(String message, Throwable cause) {
        super(message,cause);
    }
}
