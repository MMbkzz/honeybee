package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.quality.vo.QualityJobVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityJobEntity extends AbstractDataEntity<QualityJobEntity, QualityJobVo> {
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

    @Override
    public QualityJobEntity build(Long ownerId, QualityJobVo vo) {
        QualityJobEntity entity = build(ownerId);
        CommonUtil.copyProperties(vo, entity);
        return entity;
    }

    @Override
    public QualityJobEntity update(Long ownerId, QualityJobVo vo) {
        QualityJobEntity entity = update(ownerId);
        CommonUtil.copyProperties(vo, entity);
        return entity;
    }
}