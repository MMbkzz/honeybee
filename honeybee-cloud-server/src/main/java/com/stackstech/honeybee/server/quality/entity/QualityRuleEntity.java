package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleEntity extends AbstractDataEntity<QualityRuleEntity> {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    @JsonIgnore
    private String ruleConfigYaml;

    private Long jobId;

    private String desc;

    private String ruleTypeName;

    @Override
    public QualityRuleEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.ruleCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public QualityRuleEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public QualityRuleEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}