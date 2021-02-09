/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * Audit operation type
 *
 * @author William
 * @since 1.0
 */
public enum AuditOperationType implements BaseEnumTypeService {

    /**
     * SERVICE
     */
    SERVICE("Service", "SERVICE"),
    /**
     * ASSETS
     */
    ASSETS("Assets", "ASSETS"),
    /**
     * SYSTEM
     */
    SYSTEM("System", "SYSTEM"),

    /**
     * DELETE
     */
    DELETE("删除记录", "DELETE"),

    /**
     * UPDATE
     */
    UPDATE("更新记录", "UPDATE"),

    /**
     * INSERT
     */
    INSERT("新增记录", "INSERT"),

    /**
     * LOGIN
     */
    LOGIN("用户登录", "LOGIN"),

    /**
     * LOGOUT
     */
    LOGOUT("用户退出", "LOGOUT"),

    /**
     * ERROR
     */
    ERROR("系统异常", "ERROR");


    private final String name;
    private final String code;

    @Override
    public String getName() {
        return name;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

    AuditOperationType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
