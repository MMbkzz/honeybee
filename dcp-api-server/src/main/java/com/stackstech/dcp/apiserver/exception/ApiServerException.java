package com.stackstech.dcp.apiserver.exception;

public class ApiServerException extends RuntimeException {

    public ApiServerException(String message) {
        super(message);
    }

    public ApiServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
