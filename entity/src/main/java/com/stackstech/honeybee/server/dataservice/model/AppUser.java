package com.stackstech.honeybee.server.dataservice.model;

import java.sql.Timestamp;

/**
 * 服务APP用户表
 * <p>
 * app
 */
public class AppUser {

    /**
     * 主键，app用户编号，格式：APP+6位数字序列号 如：APP000001
     */
    private String id;

    /**
     * app用户名称
     */
    private String name;

    /**
     * 描述
     */
    private String appDesc;

    /**
     * app责任人
     */
    private String appOwner;
    /**
     * app责任人
     */
    private String appOwnerName;

    /**
     * 状态: active 有效 inactive 无效 deleted 删除
     */
    private String statusCode;

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

    //创建用户
    private String createUser;

    //更新用户
    private String updateUser;

    private String queryString;                 //全局搜索查询
    private String queryType;                   //查询类型public

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppOwner() {
        return appOwner;
    }

    public void setAppOwner(String appOwner) {
        this.appOwner = appOwner;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getAppOwnerName() {
        return appOwnerName;
    }

    public void setAppOwnerName(String appOwnerName) {
        this.appOwnerName = appOwnerName;
    }
}
