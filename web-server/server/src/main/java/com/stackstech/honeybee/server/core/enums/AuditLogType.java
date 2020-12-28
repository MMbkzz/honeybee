package com.stackstech.honeybee.server.core.enums;

/**
 * Audit log type defined
 *
 * @author william
 */
public enum AuditLogType {

    /**
     * AUDIT_TYPE_SERVICE
     */
    AUDIT_TYPE_SERVICE("SERVICE"),
    /**
     * AUDIT_TYPE_ASSETS
     */
    AUDIT_TYPE_ASSETS("ASSETS"),
    /**
     * AUDIT_TYPE_SYSTEM
     */
    AUDIT_TYPE_SYSTEM("SYSTEM"),

    /**
     * LOG_TYPE_TYPE_DELETE
     */
    LOG_TYPE_TYPE_DELETE("DELETE"),

    /**
     * LOG_TYPE_TYPE_UPDATE
     */
    LOG_TYPE_TYPE_UPDATE("UPDATE"),

    /**
     * LOG_TYPE_TYPE_INSERT
     */
    LOG_TYPE_TYPE_INSERT("INSERT"),

    /**
     * LOG_TYPE_TYPE_LOGIN
     */
    LOG_TYPE_TYPE_LOGIN("LOGIN"),

    /**
     * LOG_TYPE_TYPE_LOGOUT
     */
    LOG_TYPE_TYPE_LOGOUT("LOGOUT"),

    /**
     * LOG_TYPE_TYPE_ERROR
     */
    LOG_TYPE_TYPE_ERROR("ERROR"),

    /**
     * LOG_TYPE_TYPE_WARN
     */
    LOG_TYPE_TYPE_WARN("WARN");


    private final String name;

    AuditLogType(String name) {
        this.name = name;
    }

}
