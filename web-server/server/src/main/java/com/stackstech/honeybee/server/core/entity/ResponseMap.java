package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.StatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMap<T> {
    public static final String SUCCESS = "success";

    private Integer status;
    private String message;
    private Integer total;
    private String praise;
    private Long uv;
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

    private ResponseMap(T result, Integer total, String praise, Long uv) {
        this.status = StatusCode.SUCCESS.getStatus();
        this.message = SUCCESS;
        this.result = result;
        this.total = total;
        this.praise = praise;
        this.uv = uv;
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

    public static <T> ResponseMap<T> setUv(Long uv) {
        return new ResponseMap<T>(null, null, null, uv);
    }

    public static <T> ResponseMap<T> setTotal(T data, Integer total) {
        return new ResponseMap<T>(data, total, null, null);
    }

    public static <T> ResponseMap<T> setPraise(String praise) {
        return new ResponseMap<T>(null, null, praise, null);
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

    public String getPraise() {
        return praise;
    }

    public Long getUv() {
        return uv;
    }

    public T getResult() {
        return result;
    }

}
