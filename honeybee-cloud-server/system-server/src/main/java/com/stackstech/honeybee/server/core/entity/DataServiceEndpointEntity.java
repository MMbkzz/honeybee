package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEndpointEntity extends DataEntity<DataServiceEndpointEntity> {
    private Long id;

    private String dataServiceEndpointCode;

    private Long serviceNodeId;

    private Long dataServiceId;

    private Integer dataServiceResource;

    private String dataServiceEndpoint;

    private String dataServiceStatus;

    private String desc;

    @Override
    public DataServiceEndpointEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataServiceEndpointEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}