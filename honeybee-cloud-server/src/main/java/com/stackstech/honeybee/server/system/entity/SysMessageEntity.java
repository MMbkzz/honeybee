package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMessageEntity extends AbstractDataEntity<SysMessageEntity, Object> {
    private Long id;

    private String messageTitle;

    private String messageType;

    private String messageContent;

    private Long messageReceiver;

    private String desc;

    @Override
    public SysMessageEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public SysMessageEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Deprecated
    @Override
    public SysMessageEntity build(Long ownerId, Object vo) {
        return null;
    }

    @Deprecated
    @Override
    public SysMessageEntity update(Long ownerId, Object vo) {
        return null;
    }
}