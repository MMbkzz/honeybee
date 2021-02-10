package com.stackstech.honeybee.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataAuthorityMeta {

    private String paramName;
    private boolean authority;

    public DataAuthorityMeta(String paramName, boolean authority) {
        this.paramName = paramName;
        this.authority = authority;
    }

    public DataAuthorityMeta() {
    }
}
