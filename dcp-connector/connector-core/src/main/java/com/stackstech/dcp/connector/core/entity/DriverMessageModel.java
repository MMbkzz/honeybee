package com.stackstech.dcp.connector.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DriverMessageModel<T> extends DriverModel<T> {

    private String consumerId;

    private String topic;

    public DriverMessageModel(String runMode, String expression, T requestData, String consumerId) {
        super(runMode, expression, requestData);
        this.consumerId = consumerId;
    }


}
