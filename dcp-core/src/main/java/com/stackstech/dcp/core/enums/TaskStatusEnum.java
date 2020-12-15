package com.stackstech.dcp.core.enums;

public enum TaskStatusEnum {

    ok("200"),
    err("500");

    TaskStatusEnum(String code) {
        this.code = code;
    }

    public String code;
}
