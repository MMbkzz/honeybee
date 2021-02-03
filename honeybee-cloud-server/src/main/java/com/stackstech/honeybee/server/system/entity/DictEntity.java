package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictEntity extends AbstractDataEntity<DictEntity> {
    private Long id;

    private String catalogName;

    private String name;

    private String code;

    private Integer dictOrder;

    private String desc;

    @Override
    public DictEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.dictOrder = 1;
        return this;
    }

    @Override
    public DictEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DictEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }
}