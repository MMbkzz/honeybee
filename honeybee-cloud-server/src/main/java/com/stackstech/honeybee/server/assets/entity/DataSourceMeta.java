package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceMeta {

    private String paramName;
    private String paramType;
    private String paramVarName;
    private String desc;

    public DataSourceMeta(String paramName, String paramType, String paramVarName, String desc) {
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramVarName = paramVarName;
        this.desc = desc;
    }

    public DataSourceMeta() {
    }
}
