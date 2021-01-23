package com.stackstech.honeybee.server.core.enums;

/**
 * all entity status type
 *
 * @author william
 */
public enum EntityStatusType {

    /**
     * DELETE
     */
    DELETE(-1, "删除"),
    /**
     * ENABLE
     */
    ENABLE(0, "启用"),
    /**
     * DISABLE
     */
    DISABLE(1, "禁用"),
    /**
     * OFFLINE
     */
    OFFLINE(2, "下线"),
    /**
     * ONLINE
     */
    ONLINE(3, "上线");

    /**
     * status code
     */
    private final int status;
    /**
     * status desc
     */
    private final String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    EntityStatusType(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
