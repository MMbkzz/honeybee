package com.stackstech.honeybee.server.bees.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeesExceptionResponse {

    private Date timestamp = new Date();
    private int status;
    private String error;
    private String code;
    private String message;
    private String exception;
    private String path;


    BeesExceptionResponse(HttpStatus status, BeesExceptionMessage message,
                          String path) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.code = Integer.toString(message.getCode());
        this.message = message.getMessage();
        this.path = path;
    }

    BeesExceptionResponse(HttpStatus status, String message, String path,
                          String exception) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getException() {
        return exception;
    }
}
