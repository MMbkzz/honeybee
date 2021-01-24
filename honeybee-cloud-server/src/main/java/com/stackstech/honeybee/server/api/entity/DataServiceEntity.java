package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.common.utils.CommonUtil;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEntity extends DataEntity<DataServiceEntity> {

    private Long id;

    private String dataServiceName;

    private String dataServiceCode;

    private Long assetsModelId;

    private String datasourceMeta;

    private String serviceMeta;

    private Integer cacheExpire;

    private String expression;

    private String desc;

    @Override
    public DataServiceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.dataServiceCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public DataServiceEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}