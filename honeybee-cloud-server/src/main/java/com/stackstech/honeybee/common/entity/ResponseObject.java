package com.stackstech.honeybee.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject {

    private StatusCode status;
    private String message;
    private Map<String, Object> messages;
    private Integer error;
    private Integer total;
    private Object result;

    public ResponseObject success(@Nullable String message, @Nullable Object result, @Nullable Integer total) {
        this.status = StatusCode.SUCCESS;
        this.message = message;
        this.result = result;
        this.total = total;
        return this;
    }

    public ResponseObject success(String message, Object result) {
        return success(message, result, null);
    }

    public ResponseObject success(Object result, Integer total) {
        return success(StatusCode.SUCCESS.getMessage(), result, total);
    }

    public ResponseObject success(String message) {
        return success(message, null, null);
    }

    public ResponseObject success(Object result) {
        return success(StatusCode.SUCCESS.getMessage(), result);
    }

    public ResponseObject failed(StatusCode status, Map<String, Object> messages, Integer error) {
        this.status = status;
        this.messages = messages;
        this.error = error;
        return this;
    }

    public ResponseObject failed(StatusCode status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }

    public ResponseObject failed(String message) {
        return failed(StatusCode.FAILED, message);
    }

    public static ResponseObject build() {
        return new ResponseObject();
    }

}
