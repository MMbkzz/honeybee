/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.data.core.enums;

/**
 * TokenStatus
 *
 * @author William
 * @since 1.0
 */
public enum TokenStatus {

    /**
     * Token有效
     */
    VALID(0, "token valid"),
    /**
     * Token过期
     */
    EXPIRES(1, "token expires"),
    /**
     * Token无效
     */
    INVALID(2, "token invalid");

    private final Integer status;
    private final String desc;

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param status
     * @param desc
     */
    TokenStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
