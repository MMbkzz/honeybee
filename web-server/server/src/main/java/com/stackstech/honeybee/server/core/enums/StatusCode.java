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
    SUCCESS(1024, 200, "success"),
    /**
     * 失败
     */
    FAILED(2000, 200, "failed"),
    /**
     * 请求参数为空
     */
    PARAMS_NULL(2001, 200, "request parameter empty"),
    /**
     * 没有查询到数据
     */
    DATA_NULL(2002, 404, "data not found"),
    /**
     * 新增数据失败
     */
    CREATE_FAILURE(2003, 200, "insert data failed"),
    /**
     * 更新数据失败
     */
    UPDATE_FAILURE(2004, 200, "update data failed"),
    /**
     * 删除失败
     */
    DELETE_FAILURE(2005, 200, "delete data failed"),
    /**
     * 用户登录会话过期
     */
    SESSION_EXPIRED(2006, 200, "user session expired"),
    /**
     * Authorization Token无效
     */
    TOKEN_INVALID(2007, 200, "User authorization Token invalid"),
    /**
     * 参数无效，没有查询到数据
     */
    NOT_FOUND(2008, 404, "Request parameter invalid"),
    /**
     * 服务接口内部异常
     */
    INTERNAL_ERROR(3000, 500, "Internal server error"),
    /**
     * 拒绝请求
     */
    REQUEST_FORBIDDEN(3001, 403, "Forbidden"),
    /**
     * 请求方法不允许
     */
    REQUEST_UNSUPPORT(3002, 401, "Bad Request");

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
