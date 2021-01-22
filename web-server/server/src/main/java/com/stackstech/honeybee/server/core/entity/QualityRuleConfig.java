package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityRuleConfig extends DataEntity {
    private Long id;

    private Long ruleId;

    private String ruleConfigType;

    private String ruleConfigKey;

    private String ruleConfigValue;

    private String desc;

}