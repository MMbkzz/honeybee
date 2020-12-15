package com.stackstech.honeybee.apiserver.exception;


public class ServiceAccessException extends RuntimeException {

    public static ServiceAccessException create(int code) {
        return new ServiceAccessException(code);
    }

    public static ServiceAccessException create(int code, String message) {
        return new ServiceAccessException(code, message);
    }

    public static ServiceAccessException create(int code, String message, Exception rootCause) {
        return new ServiceAccessException(code, message, rootCause);
    }

    private static final long serialVersionUID = 1L;

    private final int code;

    private ServiceAccessException(int code) {
        this.code = code;
    }

    private ServiceAccessException(int code, String message) {
        super(message);
        this.code = code;
    }

    private ServiceAccessException(int code, String message, Exception rootCause) {
        super(message, rootCause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

