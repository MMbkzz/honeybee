package com.stackstech.honeybee.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceMeta {

    private String argName;
    private String argType;
    private String paramName;
    private String expression;
    private String defaultValue;
    private String desc;
    private boolean optional;

}
