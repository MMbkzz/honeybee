package com.stackstech.honeybee.server.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.StatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMap<T> {
    public static final String SUCCESS = "success";

    private final Integer status;
    private final String message;
    private Integer total;
    private T result;

    private ResponseMap() {
        this.status = StatusCode.SUCCESS.getStatus();
        this.message = SUCCESS;
    }

    private ResponseMap(String message) {
        this.status = StatusCode.SUCCESS.getStatus();
        this.message = message;
    }

    private ResponseMap(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private ResponseMap(T result) {
        this.result = result;
        this.status = StatusCode.SUCCESS.getStatus();
        this.message = SUCCESS;
    }

    private ResponseMap(T result, Integer total) {
        this.status = StatusCode.SUCCESS.getStatus();
        this.message = SUCCESS;
        this.result = result;
        this.total = total;
    }

    public static <T> ResponseMap<T> success(T data) {
        return new ResponseMap<T>(data);
    }

    public static <T> ResponseMap<T> success() {
        return new ResponseMap<T>();
    }

    public static <T> ResponseMap<T> success(String message) {
        return new ResponseMap<T>(message);
    }


    public static <T> ResponseMap<T> setTotal(T data, Integer total) {
        return new ResponseMap<T>(data, total);
    }

    public static <T> ResponseMap<T> failed(String message) {
        return new ResponseMap<T>(StatusCode.FAILED.getStatus(), message);
    }

    public static <T> ResponseMap<T> failed(StatusCode code, String message) {
        return new ResponseMap<T>(code.getStatus(), message);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTotal() {
        return total;
    }

    public T getResult() {
        return result;
    }

}
