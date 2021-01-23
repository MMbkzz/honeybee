/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.server.core.enums;

/**
 * 审计操作类型
 *
 * @author William
 * @since 1.0
 */
public enum AuditOperationType {

    /**
     * SERVICE
     */
    SERVICE("SERVICE", ""),
    /**
     * ASSETS
     */
    ASSETS("ASSETS", ""),
    /**
     * SYSTEM
     */
    SYSTEM("SYSTEM", ""),

    /**
     * DELETE
     */
    DELETE("DELETE", "删除记录"),

    /**
     * UPDATE
     */
    UPDATE("UPDATE", "更新记录"),

    /**
     * INSERT
     */
    INSERT("INSERT", "新增记录"),

    /**
     * LOGIN
     */
    LOGIN("LOGIN", "用户登录"),

    /**
     * LOGOUT
     */
    LOGOUT("LOGOUT", "用户退出"),

    /**
     * ERROR
     */
    ERROR("ERROR", "系统异常");


    private final String name;
    private final String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    AuditOperationType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
