package com.stackstech.honeybee.server.operation.vo;

/**
 * 服务审计查询VO
 */
public class ClusterAuditQueryVO {
    private String taskName;                //任务名称
    private String statusCode;              //状态
    private String startTime;               //开始时间
    private String endTime;                 //结束时间

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
