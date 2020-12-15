package com.stackstech.dcp.server.dataasset.vo;

/**
 * 资产主题查询VO类
 */
public class DataAssetTopicQueryVO {
    private String areaName;                    //资产领域名称
    private String topicName;                   //资产主题名称
    private String queryString;                 //全局搜索查询
    private String queryType;                   //查询类型public
    private String userId;
    private String owner;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
