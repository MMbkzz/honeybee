package com.stackstech.honeybee.server.operations.model;

import java.io.Serializable;

/**
 * 服务指标
 */
public class ServiceAccessKpi implements Serializable {

    private static final long serialVersionUID = -3599936156181861768L;

    private Long id;                                            //主键
    private String accessTime;                                  //访问时间(格式：YYYY-MM-DD)
    private String dataServiceId;                               //服务Id
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

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
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
