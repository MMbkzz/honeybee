package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

@Data
public class DictMapping {

    private Integer code;
    private String desc;

    public DictMapping() {

    }

    public DictMapping build(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
        return this;
    }

}
