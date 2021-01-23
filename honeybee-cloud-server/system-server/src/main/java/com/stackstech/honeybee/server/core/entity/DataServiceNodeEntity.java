package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceNodeEntity extends DataEntity<DataServiceNodeEntity> {
    private Long id;

    private String serviceNodeName;

    private String serviceNodeCode;

    private String serviceNodeIp;

    private String serviceNodePort;

    private String serviceNodeEndpoint;

    private String serviceNodeStatus;

    private String desc;

    @Override
    public DataServiceNodeEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataServiceNodeEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}