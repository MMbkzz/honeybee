package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class DataServiceNodeEntity {
    private Long id;

    private String serviceNodeName;

    private String serviceNodeCode;

    private String serviceNodeIp;

    private String serviceNodePort;

    private String serviceNodeEndpoint;

    private String serviceNodeStatus;

    private Date updatetime;

    private Date createtime;

    private String desc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceNodeName() {
        return serviceNodeName;
    }

    public void setServiceNodeName(String serviceNodeName) {
        this.serviceNodeName = serviceNodeName;
    }

    public String getServiceNodeCode() {
        return serviceNodeCode;
    }

    public void setServiceNodeCode(String serviceNodeCode) {
        this.serviceNodeCode = serviceNodeCode;
    }

    public String getServiceNodeIp() {
        return serviceNodeIp;
    }

    public void setServiceNodeIp(String serviceNodeIp) {
        this.serviceNodeIp = serviceNodeIp;
    }

    public String getServiceNodePort() {
        return serviceNodePort;
    }

    public void setServiceNodePort(String serviceNodePort) {
        this.serviceNodePort = serviceNodePort;
    }

    public String getServiceNodeEndpoint() {
        return serviceNodeEndpoint;
    }

    public void setServiceNodeEndpoint(String serviceNodeEndpoint) {
        this.serviceNodeEndpoint = serviceNodeEndpoint;
    }

    public String getServiceNodeStatus() {
        return serviceNodeStatus;
    }

    public void setServiceNodeStatus(String serviceNodeStatus) {
        this.serviceNodeStatus = serviceNodeStatus;
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