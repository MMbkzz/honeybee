package com.stackstech.honeybee.server.operation.vo;

/**
 * 平台日志VO
 */
public class SysAuditQueryVO {

    private String userName;                //用户名
    private String status;                  //状态
    private String startTime;               //开始时间
    private String endTime;                 //结束时间

    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页显示条数
     */
    private Integer pageSize;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
