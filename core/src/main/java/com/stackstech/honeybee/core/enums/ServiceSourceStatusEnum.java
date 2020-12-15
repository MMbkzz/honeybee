package com.stackstech.honeybee.core.enums;

public enum ServiceSourceStatusEnum {

    /**
     * 启用
     */
    enabled("enabled", "启用")
    /** 禁用 */
    , disabled("disabled", "禁用")
    /** 删除 */
    , deleted("deleted", "删除");

    public String code;
    public String desc;

    ServiceSourceStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
