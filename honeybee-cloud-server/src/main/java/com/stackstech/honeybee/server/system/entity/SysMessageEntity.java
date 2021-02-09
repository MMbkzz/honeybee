package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import com.stackstech.honeybee.server.core.enums.types.MessageType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMessageEntity extends AbstractDataEntity<SysMessageEntity> {
    private Long id;

    private String messageTitle;

    private MessageType messageType;

    private String messageTypeName;

    private String messageContent;

    private Long messageReceiver;

    private String desc;

    public String getMessageTypeName() {
        return messageType.getName();
    }

    @Override
    public SysMessageEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
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

    @Override
    public SysMessageEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}