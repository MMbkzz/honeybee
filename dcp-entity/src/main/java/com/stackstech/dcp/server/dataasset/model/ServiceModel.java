package com.stackstech.dcp.server.dataasset.model;


import java.sql.Timestamp;

/**
 * 服务模型实体类
 * <p>
 * <p>
 * <p>
 * dcp_service_model
 */
public class ServiceModel {

    /**
     * 数据模型ID
     */
    private String id;

    /**
     * 父模型ID
     */
    private String parentId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 数据模型描述
     */
    private String modelDesc;

    /**
     * 模型类型：data 数据模型 message 消息模型 api api模型
     */
    private String typeCode;

    /**
     * 关联dcp_data_model_topic主键ID
     */
    private String topicId;

    /**
     * 关联dcp_datasource主键ID
     */
    private String serviceSourceId;

    /**
     * 状态: published 已发布 initialized  待发布 deleted 删除
     */
    private String statusCode;

    /**
     * 请求方法(POST/GET  api模型)
     */
    private String requestMethod;

    /**
     * 数据模型内容获取类型：sql/table/topic/url
     */
    private String expressionType;

    /**
     * sql表达式、表名、topic名、Url
     */
    private String expression;

    /**
     * 缓存时间，秒，0代表不缓存
     */
    private Integer cacheDuration;

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

    //创建用户
    private String createUser;

    //更新用户
    private String updateUser;

    //body模板
    private String bodyPattern;

    /**
     * 修改人
     */
    private Long updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getCacheDuration() {
        return cacheDuration;
    }

    public void setCacheDuration(Integer cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    public String getServiceSourceId() {
        return serviceSourceId;
    }

    public void setServiceSourceId(String serviceSourceId) {
        this.serviceSourceId = serviceSourceId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getBodyPattern() {
        return bodyPattern;
    }

    public void setBodyPattern(String bodyPattern) {
        this.bodyPattern = bodyPattern;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
