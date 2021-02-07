package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * System message type
 *
 * @author william
 * @since 1.0
 */
public enum MessageType implements BaseEnumTypeService {
    /**
     * System
     */
    SYSTEM("系统消息", "SYSTEM");

    private final String name;
    private final String code;

    @JsonValue
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    MessageType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
