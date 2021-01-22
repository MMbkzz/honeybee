package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class QualityRuleEntity extends DataEntity {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    private String ruleConfigYaml;

    private Long jobId;

    private String desc;

}