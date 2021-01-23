package com.stackstech.honeybee.server.system.entity;

import lombok.Data;

@Data
public class DictMapping {

    private String code;
    private String desc;

    public DictMapping() {

    }

    public DictMapping build(Integer code, String desc) {
        this.code = String.valueOf(code);
        this.desc = desc;
        return this;
    }

    public DictMapping build(String code, String desc) {
        this.code = code;
        this.desc = desc;
        return this;
    }

}
