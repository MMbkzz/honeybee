package com.stackstech.honeybee.server.core.exception;

/**
 * Data not found exception
 *
 * @author william
 * @since 1.0
 */
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
