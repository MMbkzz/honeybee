package com.stackstech.dcp.server.dataservice.vo;

import java.util.List;

/**
 * 数据服务查询VO类
 */
public class DataServiceQueryVO {

    private String areaName;                    //资产领域名称
    private String topicName;                   //资产主题名称
    private String modelName;                   //模型名称
    private String dataServiceName;             //服务名称
    private String dataServiceId;               //服务ID
    private String typeCode;                    //模型类型
    private String statusCode;                  //服务状态
    private String queryString;                 //全局搜索查询
    private String queryType;                   //查询类型public
    private String userId;
    private List<String> roleCodes;

    public DataServiceQueryVO(String areaName, String topicName, String modelName, String dataServiceName, String dataServiceId, String typeCode, String statusCode, String queryString, String userId) {
        this.areaName = areaName;
        this.topicName = topicName;
        this.modelName = modelName;
        this.dataServiceName = dataServiceName;
        this.dataServiceId = dataServiceId;
        this.typeCode = typeCode;
        this.statusCode = statusCode;
        this.queryString = queryString;
        this.userId = userId;
    }

    public List<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(List<String> roleCodes) {
        this.roleCodes = roleCodes;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDataServiceName() {
        return dataServiceName;
    }

    public void setDataServiceName(String dataServiceName) {
        this.dataServiceName = dataServiceName;
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

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
