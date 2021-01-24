package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleConfig extends DataEntity<QualityRuleConfig> {
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
}