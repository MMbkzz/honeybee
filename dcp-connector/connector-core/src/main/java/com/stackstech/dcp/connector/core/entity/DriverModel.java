package com.stackstech.dcp.connector.core.entity;

import lombok.Data;

@Data
public class DriverModel<T> {

    /**
     * 运行方式   read : 读取, write : 写入
     */
    private String runMode;

    /**
     * sql表达式、表名、topic名,URL
     */
    private String expression;

    private T requestData;

    public DriverModel() {
    }

    public DriverModel(String runMode, String expression, T requestData) {
        this.runMode = runMode;
        this.expression = expression;
        this.requestData = requestData;
    }

}
