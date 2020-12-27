package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class DataServiceEndpointEntity {
    private Long id;

    private String dataServiceEndpointCode;

    private Long serviceNodeId;

    private Long dataServiceId;

    private Integer dataServiceResource;

    private String dataServiceEndpoint;

    private String dataServiceStatus;

    private Date updatetime;

    private Date createtime;

    private String desc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataServiceEndpointCode() {
        return dataServiceEndpointCode;
    }

    public void setDataServiceEndpointCode(String dataServiceEndpointCode) {
        this.dataServiceEndpointCode = dataServiceEndpointCode;
    }

    public Long getServiceNodeId() {
        return serviceNodeId;
    }

    public void setServiceNodeId(Long serviceNodeId) {
        this.serviceNodeId = serviceNodeId;
    }

    public Long getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(Long dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public Integer getDataServiceResource() {
        return dataServiceResource;
    }

    public void setDataServiceResource(Integer dataServiceResource) {
        this.dataServiceResource = dataServiceResource;
    }

    public String getDataServiceEndpoint() {
        return dataServiceEndpoint;
    }

    public void setDataServiceEndpoint(String dataServiceEndpoint) {
        this.dataServiceEndpoint = dataServiceEndpoint;
    }

    public String getDataServiceStatus() {
        return dataServiceStatus;
    }

    public void setDataServiceStatus(String dataServiceStatus) {
        this.dataServiceStatus = dataServiceStatus;
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