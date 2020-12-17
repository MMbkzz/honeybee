package com.stackstech.honeybee.server.bees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
public abstract class BeesException extends RuntimeException {

    BeesException(String message) {
        super(message);
    }

    BeesException(String message, Throwable cause) {
        super(message, cause);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class NotFoundException extends BeesException {
        public NotFoundException(BeesExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ConflictException extends BeesException {
        public ConflictException(BeesExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends BeesException {
        public BadRequestException(BeesExceptionMessage message) {
            super(message.toString());
        }
    }

    public static class ServiceException extends BeesException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UnImplementedException extends BeesException {
        public UnImplementedException(String message) {
            super(message);
        }
    }
}
