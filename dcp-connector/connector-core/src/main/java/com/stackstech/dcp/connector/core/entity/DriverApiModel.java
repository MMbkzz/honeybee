package com.stackstech.dcp.connector.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DriverApiModel<T> extends DriverModel<T> {

    private String requestMethod;

    public DriverApiModel(String runMode, String expression, String requestMethod, T requestData) {
        super(runMode, expression, requestData);
        this.requestMethod = requestMethod;
    }

}
