package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class QualityRuleConfigEntity {
    private Long id;

    private Long ruleId;

    private String ruleConfigType;

    private String ruleConfigKey;

    private String ruleConfigValue;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}