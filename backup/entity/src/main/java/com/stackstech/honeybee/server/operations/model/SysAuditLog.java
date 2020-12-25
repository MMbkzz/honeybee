package com.stackstech.honeybee.server.operations.model;

import java.sql.Timestamp;

/**
 * sys_audit_log
 */
public class SysAuditLog {
    private Long id;                            // 主键，自增

    private Long userId;                        // 操作用户ID

    private String ip;                          // 访问IP

    private String status;                      // 操作状态. 200 成功, 400失败 …

    private String api;                          // 操作接口

    private String apiDesc;                      // 操作接口描述

    private Timestamp requestTime;               // 访问时间

    private Long responseTime;                  // 响应时间（单位：毫秒）

    private Timestamp createTime;               // 创建时间

    private Long createBy;                      // 创建人

    private Timestamp updateTime;               // 修改时间

    private Long updateBy;                      // 修改人

    private String userName;                    //用户名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
