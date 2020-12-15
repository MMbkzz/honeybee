package com.stackstech.dcp.connector.core.exception;

public class DriverExecutorException extends RuntimeException {

    public DriverExecutorException(String message) {
        super(message);
    }

    public DriverExecutorException(String message, Throwable cause) {
        super(message, cause);
    }
}
