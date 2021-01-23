package com.stackstech.honeybee.server.api.entity;

import lombok.Data;

@Data
public class DataServiceParameter {
    private Long id;
    private String argName;
    private String argType;
    private String paramName;
    private String expression;
    private String defaultValue;
    private String desc;
    private boolean optional;
}
