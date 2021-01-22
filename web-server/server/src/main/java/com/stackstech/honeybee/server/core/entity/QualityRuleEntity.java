package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleEntity extends DataEntity<QualityRuleEntity> {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    @JsonIgnore
    private String ruleConfigYaml;

    private Long jobId;

    private String desc;

}