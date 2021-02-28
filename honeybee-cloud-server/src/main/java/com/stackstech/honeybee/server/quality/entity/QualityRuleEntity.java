package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import com.stackstech.honeybee.server.core.enums.types.QualityRuleType;
import com.stackstech.honeybee.server.quality.vo.QualityRuleVo;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleEntity extends AbstractDataEntity<QualityRuleEntity> {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private QualityRuleType ruleType;

    private String ruleTypeName;

    private String ruleExpressionType;

    private JsonParameterList ruleExpression;

    @JsonIgnore
    private String ruleConfigYaml;

    private Long jobId;

    private String desc;

    private Map<String, Object> job;

    @JsonIgnore
    private QualityRuleVo qualityRuleVo;

    public String getRuleTypeName() {
        return ruleType.getName();
    }

    @Override
    public QualityRuleEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
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
        this.qualityRuleVo = (QualityRuleVo) vo;
        return this;
    }

}