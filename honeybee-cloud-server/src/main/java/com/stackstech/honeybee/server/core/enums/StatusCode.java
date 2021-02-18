package com.stackstech.honeybee.server.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * API Server code
 *
 * @author William
 * @since 1.0
 */
public enum StatusCode {

    /**
     * 成功
     */
    SUCCESS(1000, "Success"),
    /**
     * 失败
     */
    FAILED(2000, "Failed"),
    /**
     * Authorization Token无效
     */
    UNAUTHORIZED(3000, "User authorization token invalid"),
    /**
     * 服务接口内部异常
     */
    INTERNAL_ERROR(3001, "Internal server error"),
    /**
     * 拒绝请求
     */
    FORBIDDEN(3002, "Forbidden"),
    /**
     * 请求方法不允许
     */
    BAD_REQUEST(3003, "Bad Request");

    @JsonValue
    private final int status;
    private final String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    StatusCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
