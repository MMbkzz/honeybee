package com.stackstech.honeybee.server.operation.vo;

/**
 *
 */
public class ServiceMonitorQueryVO {

    private String dateIndex;
    private String queryDate;
    private String dataServiceId;
    private String accessType;

    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页显示条数
     */
    private Integer pageSize;

    public String getDateIndex() {
        return dateIndex;
    }

    public void setDateIndex(String dateIndex) {
        this.dateIndex = dateIndex;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
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

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}
