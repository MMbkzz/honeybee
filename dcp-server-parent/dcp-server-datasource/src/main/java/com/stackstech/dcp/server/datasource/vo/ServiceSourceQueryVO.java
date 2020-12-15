package com.stackstech.dcp.server.datasource.vo;

/**
 * 数据源查询VO类
 */
public class ServiceSourceQueryVO {
    private String driverName;                          //驱动名称
    private String serviceSourceName;                   //数据源名称
    private String statusCode;                          //数据源状态
    private String queryString;                         //全局搜索查询
    private String queryType;                           //查询类型public

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
