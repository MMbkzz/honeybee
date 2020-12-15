package com.stackstech.honeybee.server.dataasset.model;

import java.sql.Timestamp;

/**
 * 服务模型参数实体
 * <p>
 * <p>
 * dcp_service_model_param
 */
public class ServiceModelParam {

    /**
     * ID，数字序列，主键：datamodel_id+ID
     */
    private Long id;

    /**
     * 关联dcp_data_model主键ID
     */
    private String serviceModelId;

    /**
     * 参数名称，header参数content_type为必选
     */
    private String paramName;

    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 操作类型(大于/小于/等于)
     */
    private String operateType;

    /**
     * 数字序列
     */
    private Integer seqNum;

    /**
     * 参数值
     */
    private String defaultValue;

    /**
     * 描述
     */
    private String paramDesc;

    /**
     * 参数所在位置，仅对api模型有效, header/body, 其中header参数content_type为必选
     */
    private String paramTypeCode;

    /**
     * 是否必填
     */
    private String isRequired;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private Long updateBy;

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getParamTypeCode() {
        return paramTypeCode;
    }

    public void setParamTypeCode(String paramTypeCode) {
        this.paramTypeCode = paramTypeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getServiceModelId() {
        return serviceModelId;
    }

    public void setServiceModelId(String serviceModelId) {
        this.serviceModelId = serviceModelId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

   /* @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceModelParam) {
            if (this.getParamName().equals(((ServiceModelParam) obj).getParamName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }*/
}
