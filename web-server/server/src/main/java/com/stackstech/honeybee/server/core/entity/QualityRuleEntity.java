package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class QualityRuleEntity {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    private String ruleConfigYaml;

    private Long jobId;

    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long owner;

    @ApiModelProperty(hidden = true)
    private Date updatetime;

    @ApiModelProperty(hidden = true)
    private Date createtime;


    private String desc;

}