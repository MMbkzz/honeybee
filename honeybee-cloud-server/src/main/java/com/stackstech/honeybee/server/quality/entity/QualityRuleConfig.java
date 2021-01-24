package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleConfig extends AbstractDataEntity<QualityRuleConfig, Object> {
    private Long id;

    private Long ruleId;

    private String ruleConfigType;

    private String ruleConfigKey;

    private String ruleConfigValue;

    private String desc;

    @Override
    public QualityRuleConfig build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public QualityRuleConfig update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Deprecated
    @Override
    public QualityRuleConfig build(Long ownerId, Object vo) {
        return null;
    }

    @Deprecated
    @Override
    public QualityRuleConfig update(Long ownerId, Object vo) {
        return null;
    }
}