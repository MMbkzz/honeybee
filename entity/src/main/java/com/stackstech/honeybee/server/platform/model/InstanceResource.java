package com.stackstech.honeybee.server.platform.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 实例Resource
 */
public class InstanceResource implements Serializable {

    private static final long serialVersionUID = 1383456840260316335L;

    private Long id;                            //主键Id
    private String instanceId;                  //资源Id
    private String serviceSourceId;             //数据源Id
    private int seqNum;                         //序列
    private String resourceTypeCode;            //资源类型Code
    private Integer expectNumber;               //预期资源数
    private Timestamp createTime;               //创建时间
    private Long createBy;                      //创建人
    private Timestamp updateTime;               //创建时间
    private Long updateBy;                      //创建人

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getServiceSourceId() {
        return serviceSourceId;
    }

    public void setServiceSourceId(String serviceSourceId) {
        this.serviceSourceId = serviceSourceId;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
    }

    public Integer getExpectNumber() {
        return expectNumber;
    }

    public void setExpectNumber(Integer expectNumber) {
        this.expectNumber = expectNumber;
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
