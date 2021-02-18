package com.stackstech.honeybee.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Response object
 *
 * @author william
 * @since 1.0
 */
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

    /**
     * get success response object
     *
     * @param message message
     * @param result  data
     * @param total   data size
     * @return ResponseObject
     */
    protected ResponseObject success(@Nullable String message, @Nullable Object result, @Nullable Integer total) {
        this.status = StatusCode.SUCCESS;
        this.message = message;
        this.result = result;
        this.total = total;
        return this;
    }

    /**
     * get success response object
     *
     * @param message message
     * @param result  data
     * @return ResponseObject
     */
    public ResponseObject success(String message, Object result) {
        return success(message, result, null);
    }

    /**
     * get success response object
     *
     * @param result data
     * @param total  data size
     * @return ResponseObject
     */
    public ResponseObject success(Object result, Integer total) {
        return success(StatusCode.SUCCESS.getMessage(), result, total);
    }

    /**
     * get success response object
     *
     * @param message message
     * @return ResponseObject
     */
    public ResponseObject success(String message) {
        return success(message, null, null);
    }

    /**
     * get success response object
     *
     * @param result data
     * @return ResponseObject
     */
    public ResponseObject success(Object result) {
        return success(StatusCode.SUCCESS.getMessage(), result, null);
    }

    /**
     * get failed response object
     *
     * @param status   status
     * @param messages multi line error message
     * @param error    error count
     * @return ResponseObject
     * @see StatusCode
     */
    public ResponseObject failed(StatusCode status, Map<String, Object> messages, Integer error) {
        this.status = status;
        this.messages = messages;
        this.error = error;
        return this;
    }

    /**
     * get failed response object
     *
     * @param status  status
     * @param message message
     * @return ResponseObject
     * @see StatusCode
     */
    public ResponseObject failed(StatusCode status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }

    /**
     * get default failed response object
     *
     * @param message message
     * @return ResponseObject
     */
    public ResponseObject failed(String message) {
        return failed(StatusCode.FAILED, message);
    }

    /**
     * message i18n
     *
     * @return ResponseObject
     */
    public ResponseObject of() {
        this.message = MessageHandler.of().message(message);
        return this;
    }

    /**
     * build a new response object
     *
     * @return ResponseObject
     */
    public static ResponseObject build() {
        return new ResponseObject();
    }

}
