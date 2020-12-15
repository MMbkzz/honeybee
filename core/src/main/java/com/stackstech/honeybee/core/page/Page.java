package com.stackstech.honeybee.core.page;

import java.io.Serializable;

/**
 * Page类的相关信息
 */
public class Page implements Serializable {
    private static final long serialVersionUID = 2134592479095420008L;

    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPages;
    /**
     * 总条数
     */
    private Integer totalElements;
    /**
     * 当页总条数
     */
    private Integer numberElements;
    /**
     * 通用 排序
     */
    private String orderBy;

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

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getNumberElements() {
        return numberElements;
    }

    public void setNumberElements(Integer numberElements) {
        this.numberElements = numberElements;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
