package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * all entity status type
 *
 * @author william
 * @since 1.0
 */
public enum EntityStatusType implements BaseEnumTypeService {

    /**
     * DELETE
     */
    DELETE("删除", "-1"),
    /**
     * ENABLE
     */
    ENABLE("启用", "0"),
    /**
     * DISABLE
     */
    DISABLE("禁用", "1"),
    /**
     * OFFLINE
     */
    OFFLINE("下线", "2"),
    /**
     * ONLINE
     */
    ONLINE("上线", "3");

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

    EntityStatusType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
