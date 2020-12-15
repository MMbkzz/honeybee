package com.stackstech.dcp.server.operations.model;

import java.sql.Timestamp;

/**
 * dcp_audit_log
 */
public class AuditLog {

    private Integer id;                         // 主键，自增

    private String taskName;                   // 任务名称(master-heartbeat/ master-resource/master-instance-stage)

    private String host;                       // IP地址

    private String port;                       // 端口

    private String thread;                     // 进程号

    private String statusCode;                 // 操作

    private String message;                    // 返回信息

    private String context;                    // 返回信息

    private Timestamp createTime;             // 创建时间

    private Long createBy;                    // 创建人

    private Timestamp updateTime;             // 修改时间

    private Long updateBy;                    // 修改时间

    private Timestamp taskStartTime;          // 任务开始时间

    private Timestamp taskEndTime;            // 任务结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public Timestamp getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Timestamp taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Timestamp getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Timestamp taskEndTime) {
        this.taskEndTime = taskEndTime;
    }
}
