package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceEntity extends AbstractDataEntity<DataSourceEntity> {
    private Long id;

    private String datasourceName;

    private String datasourceCode;

    private String datasourceType;

    private JsonParameterMap datasourceConfig;

    private String desc;

    private String datasourceTypeName;

    @Override
    public DataSourceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.datasourceCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public DataSourceEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataSourceEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}