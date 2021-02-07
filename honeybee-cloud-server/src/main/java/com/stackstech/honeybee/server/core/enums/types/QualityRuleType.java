package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * Quality rule type
 *
 * @author william
 * @since 1.0
 */
public enum QualityRuleType implements BaseEnumTypeService {
    /**
     * ACCURACY
     */
    ACCURACY("精确性", "ACCURACY"),
    /**
     * PROFILING
     */
    PROFILING("一致性", "PROFILING"),
    /**
     * UNIQUENESS
     */
    UNIQUENESS("唯一性", "UNIQUENESS"),
    /**
     * DISTINCT
     */
    DISTINCT("有效性", "DISTINCT"),
    /**
     * TIMELINESS
     */
    TIMELINESS("及时性", "TIMELINESS"),
    /**
     * COMPLETENESS
     */
    COMPLETENESS("完整性", "COMPLETENESS");

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

    QualityRuleType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
