package com.stackstech.honeybee.server.core.exception;

/**
 * Authentication exception
 *
 * @author william
 * @since 1.0
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
