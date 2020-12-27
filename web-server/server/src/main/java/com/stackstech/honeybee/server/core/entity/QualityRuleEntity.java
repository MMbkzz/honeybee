package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class QualityRuleEntity {
    private Long id;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    private String ruleConfigYaml;

    private Long jobId;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}