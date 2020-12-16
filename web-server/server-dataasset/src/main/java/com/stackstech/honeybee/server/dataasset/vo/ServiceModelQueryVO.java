package com.stackstech.honeybee.server.dataasset.vo;

import java.util.List;

/**
 *
 */
public class ServiceModelQueryVO {

    private String areaName;                //资产领域名称
    private String topicName;               //资产主题名称
    private String serviceSourceName;       //数据源名称
    private String modelName;               //服务模型名称
    private String typeCode;                //模型类型
    private String statusCode;              //服务模型状态
    private String requestMethod;           //发布类型<POST/GET>
    private String queryString;             //全局搜索查询
    private String queryType;               //查询类型public

    private String userId;
    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getServiceSourceName() {
        return serviceSourceName;
    }

    public void setServiceSourceName(String serviceSourceName) {
        this.serviceSourceName = serviceSourceName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}
