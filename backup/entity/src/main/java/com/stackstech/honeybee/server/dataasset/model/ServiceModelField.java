package com.stackstech.honeybee.server.dataasset.model;

import java.sql.Timestamp;

/**
 * 服务模型字段实体
 * <p>
 * <p>
 * service_model_field
 */
public class ServiceModelField {

    /**
     * ID自增
     */
    private Long id;

    /**
     * 关联data_model主键ID
     */
    private String serviceModelId;

    /**
     * 数字序列，datamodel_id+seq_num唯一
     */
    private Integer seqNum;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 字段表达式
     */
    private String expression;

    /**
     * 字段描述
     */
    private String fieldDesc;

    /**
     * 是否是派生字段
     */
    private String isDerived;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getIsDerived() {
        return isDerived;
    }

    public void setIsDerived(String isDerived) {
        this.isDerived = isDerived;
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


    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getServiceModelId() {
        return serviceModelId;
    }

    public void setServiceModelId(String serviceModelId) {
        this.serviceModelId = serviceModelId;
    }

    /*@Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceModelField) {
            if (this.getFieldName().equals(((ServiceModelField) obj).getFieldName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }*/
}
