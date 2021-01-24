package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.common.utils.CommonUtil;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityJobEntity extends DataEntity<QualityJobEntity> {
    private Long id;

    private String jobName;

    private String jobCode;

    private String jobExpression;

    private Integer jobOrder;

    private String desc;

    @Override
    public QualityJobEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.jobCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public QualityJobEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}