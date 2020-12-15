package com.stackstech.dcp.server.operation.vo;

/**
 * 数据源监控查询VO
 */
public class SourceMonitorQueryVO {
    private String instanceName;                        //实例名称
    private String serviceSourceName;                   //数据源名称
    private String statusCode;                          //状态码

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getServiceSourceName() {
        return serviceSourceName;
    }

    public void setServiceSourceName(String serviceSourceName) {
        this.serviceSourceName = serviceSourceName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
