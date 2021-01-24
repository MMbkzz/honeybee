package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.common.utils.CommonUtil;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceTenantEntity extends DataEntity<DataServiceTenantEntity> {
    private Long id;

    private String tenantName;

    private String tenantCode;

    private String desc;

    @Override
    public DataServiceTenantEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.tenantCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public DataServiceTenantEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}