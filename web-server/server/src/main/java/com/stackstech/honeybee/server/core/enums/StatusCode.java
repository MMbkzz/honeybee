package com.stackstech.honeybee.server.core.enums;

/**
 * 服务接口响应状态码
 *
 * @author William
 * @date 2019-03-01
 * @since 1.0
 */
public enum StatusCode {

    /**
     * 成功
     */
    SUCCESS(2000, 200, "Success"),
    /**
     * 失败
     */
    FAILED(3000, 200, "Failed"),
    /**
     * 请求参数为空
     */
    PARAMS_NULL(3001, 200, "Request parameter empty"),
    /**
     * 没有查询到数据
     */
    NOT_FOUND(3002, 404, "Data not found"),
    /**
     * Authorization Token无效
     */
    UNAUTHORIZED(4001, 401, "User authorization Token invalid"),
    /**
     * 服务接口内部异常
     */
    INTERNAL_ERROR(5001, 500, "Internal server error"),
    /**
     * 拒绝请求
     */
    FORBIDDEN(5002, 403, "Forbidden"),
    /**
     * 请求方法不允许
     */
    BAD_REQUEST(5003, 400, "Bad Request");

    private final int status;
    private final int httpCode;
    private final String message;

    public int getStatus() {
        return status;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }


    StatusCode(int status, int httpCode, String message) {
        this.status = status;
        this.httpCode = httpCode;
        this.message = message;
    }

}
