package com.stackstech.honeybee.server.audit.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogEntity extends AbstractDataEntity<AuditLogEntity> {
    private Long id;

    private String logTitle;

    private String logAudit;

    private String logType;

    private String logContent;

    private String desc;

    private String logTypeName;

    @Override
    public AuditLogEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AuditLogEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AuditLogEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }


}