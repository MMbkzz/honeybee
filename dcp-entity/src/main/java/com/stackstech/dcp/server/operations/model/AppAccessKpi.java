package com.stackstech.dcp.server.operations.model;

import java.io.Serializable;

/**
 * APP用户指标
 */
public class AppAccessKpi implements Serializable {

    private static final long serialVersionUID = -4546223853195758435L;

    private Long id;                                            //主键
    private String accessTime;                                  //访问时间(格式：YYYY-MM-DD)
    private String appId;                                       //appId
    private String statusCode;                                  //状态
    private Long totalAccessNum;                                //累计访问次数
    private Long totalAccessTime;                               //累计次数
    private Long totalExecuteTime;                              //累计执行时长

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getTotalAccessNum() {
        return totalAccessNum;
    }

    public void setTotalAccessNum(Long totalAccessNum) {
        this.totalAccessNum = totalAccessNum;
    }

    public Long getTotalAccessTime() {
        return totalAccessTime;
    }

    public void setTotalAccessTime(Long totalAccessTime) {
        this.totalAccessTime = totalAccessTime;
    }

    public Long getTotalExecuteTime() {
        return totalExecuteTime;
    }

    public void setTotalExecuteTime(Long totalExecuteTime) {
        this.totalExecuteTime = totalExecuteTime;
    }
}
