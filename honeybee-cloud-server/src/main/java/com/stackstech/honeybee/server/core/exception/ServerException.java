package com.stackstech.honeybee.server.core.exception;

/**
 * Server exception
 *
 * @author william
 * @since 1.0
 */
public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
