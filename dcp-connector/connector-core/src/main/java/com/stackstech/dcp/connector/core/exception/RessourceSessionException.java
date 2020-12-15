package com.stackstech.dcp.connector.core.exception;

public class RessourceSessionException extends RuntimeException {

    public RessourceSessionException(String message) {
        super(message);
    }

    public RessourceSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
