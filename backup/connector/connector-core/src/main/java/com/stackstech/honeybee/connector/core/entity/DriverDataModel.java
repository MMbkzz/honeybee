package com.stackstech.honeybee.connector.core.entity;

public class DriverDataModel<T> extends DriverModel<T> {

    public DriverDataModel(String runMode, String expression, T requestData) {
        super(runMode, expression, requestData);
    }

}