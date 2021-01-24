package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceAuthorityEntity extends DataEntity<DataServiceAuthorityEntity> {
    private Long id;

    private Long tenantId;

    private Long dataServiceId;

    private String authorityToken;

    private Long authorityExpire;

    private String authorityData;

    private String desc;

    @Override
    public DataServiceAuthorityEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataServiceAuthorityEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}