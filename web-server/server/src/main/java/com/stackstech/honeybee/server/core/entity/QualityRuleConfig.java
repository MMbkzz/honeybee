package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class QualityRuleConfig {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleConfigType() {
        return ruleConfigType;
    }

    public void setRuleConfigType(String ruleConfigType) {
        this.ruleConfigType = ruleConfigType;
    }

    public String getRuleConfigKey() {
        return ruleConfigKey;
    }

    public void setRuleConfigKey(String ruleConfigKey) {
        this.ruleConfigKey = ruleConfigKey;
    }

    public String getRuleConfigValue() {
        return ruleConfigValue;
    }

    public void setRuleConfigValue(String ruleConfigValue) {
        this.ruleConfigValue = ruleConfigValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}