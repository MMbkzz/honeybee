package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigEntity extends AbstractDataEntity<SysConfigEntity> {
    private Long id;

    private String configKey;

    private String configValue;

    private String desc;

    @Override
    public SysConfigEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public SysConfigEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public SysConfigEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}