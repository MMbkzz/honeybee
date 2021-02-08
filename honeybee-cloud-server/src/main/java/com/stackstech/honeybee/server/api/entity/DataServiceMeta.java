package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceMeta {

    private String argName;
    private String argType;
    private String paramName;
    private String expression;
    private String defaultValue;
    private String desc;
    private boolean optional;

    public DataServiceMeta(String argName, String argType, String paramName, String expression, String defaultValue, String desc, boolean optional) {
        this.argName = argName;
        this.argType = argType;
        this.paramName = paramName;
        this.expression = expression;
        this.defaultValue = defaultValue;
        this.desc = desc;
        this.optional = optional;
    }

    public DataServiceMeta() {
    }
}
