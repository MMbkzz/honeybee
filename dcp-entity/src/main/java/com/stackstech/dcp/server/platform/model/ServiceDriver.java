package com.stackstech.dcp.server.platform.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 驱动管理实体类
 * <p>
 * dcp_service_driver
 */
public class ServiceDriver implements Serializable {

    private static final long serialVersionUID = 1351013900512510133L;

    private String id;                          //主键(DRI+6位数字序列号)
    private String driverName;                  //驱动名称
    private String version;                     //驱动版本
    private String driverDesc;                  //驱动描述
    private String driverPath;                  //驱动包路径
    private String driverClass;                 //驱动主类
    private String statusCode;                  //状态码(enabled/disabled/deleted)
    private Timestamp createTime;               //创建时间
    private Long createBy;                      //创建人
    private Timestamp updateTime;               //更新时间
    private Long updateBy;                      //更新人

    private String createUser;                  //创建用户
    private String updateUser;                  //更新用户
    private String queryString;                 //全局搜索查询
    private String queryType;                   //查询类型public

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDriverDesc() {
        return driverDesc;
    }

    public void setDriverDesc(String driverDesc) {
        this.driverDesc = driverDesc;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
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
}
