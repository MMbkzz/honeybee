package com.stackstech.honeybee.server.api.entity;

import lombok.Data;

@Data
public class DataServiceElement {

    private Long id;
    private String dataServiceName;

    public DataServiceElement(Long id, String dataServiceName) {
        this.id = id;
        this.dataServiceName = dataServiceName;
    }
}
