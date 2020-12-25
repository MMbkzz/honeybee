package com.stackstech.honeybee.server.dataservice.model;

import java.sql.Timestamp;

/**
 * APP授权字段
 * <p>
 * app_ds_field
 */
public class AppDsField {

    private Long id;
    /**
     * 关联api_user主键ID
     */
    private String appId;

    /**
     * 关联data_service 主键id
     */
    private String dataServiceId;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段id
     */
    private String fieldId;

    /**
     * 数字序列
     */
    private Integer seqNum;

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

    //字段类型
    private String dataType;

    //字段描述
    private String fieldDesc;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
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


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppDsField) {
            return this.getFieldName().equals(((AppDsField) obj).getFieldName());
        }
        return false;
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

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }
}
