package com.stackstech.honeybee.server.platform.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 系统配置参数实体类
 * <p>
 * dgp_sys_config
 */
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 7396240093451744172L;

    private Long id;                        //主键
    private String configType;              //参数大类
    private String configSubtype;           //参数小类
    private String defaultValue;            //默认名称
    private String configName;              //配置参数名称
    private String configValue;             //配置参数值
    private String displayName;             //显示名称
    private String configDesc;              //配置参数描述
    private String readOnly;                //只读
    private Timestamp createTime;           //创建时间
    private Long createBy;                  //创建人
    private Timestamp updateTime;           //更新时间
    private Long updateBy;                  //更新人

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigSubtype() {
        return configSubtype;
    }

    public void setConfigSubtype(String configSubtype) {
        this.configSubtype = configSubtype;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
}
