package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEndpointEntity extends AbstractDataEntity<DataServiceEndpointEntity, Object> {
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

    @Deprecated
    @Override
    public DataServiceEndpointEntity build(Long ownerId, Object vo) {
        return null;
    }

    @Deprecated
    @Override
    public DataServiceEndpointEntity update(Long ownerId, Object vo) {
        return null;
    }
}