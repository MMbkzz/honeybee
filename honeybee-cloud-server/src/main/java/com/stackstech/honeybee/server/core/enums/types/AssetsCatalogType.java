package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * Assets catalog type
 *
 * @author william
 * @since 1.0
 */
public enum AssetsCatalogType implements BaseEnumTypeService {

    /**
     * assets domain
     */
    DOMAIN("数据资产领域", "DOMAIN"),
    /**
     * assets topic
     */
    TOPIC("数据资产主题", "TOPIC");

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

    AssetsCatalogType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
