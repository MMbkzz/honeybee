package com.stackstech.honeybee.server.core.entity;


import lombok.Data;

import java.util.Optional;

/**
 * 请求的参数实体，包含所有服务接口的请求参数定义
 *
 * @author William
 */
@Data
public class RequestParameter {

    /**
     * page limit start index
     */
    private Integer pageStart = 0;
    /**
     * page limit record size
     */
    private Integer pageSize = 0;
    /**
     * search keywords
     */
    private String keywords;
    /**
     * query start time
     */
    private Long startTime;
    /**
     * query end time
     */
    private Long endTime;
    /**
     * query by order
     */
    private String order;
    /**
     * query by types
     */
    private Integer type;


    public String getOrder() {
        return Optional.ofNullable(order).orElse("`createtime` desc");
    }

    public Integer getPageStart() {
        if (pageStart != null && pageStart > 1) {
            pageStart = (pageStart - 1) * pageSize;
        } else {
            pageStart = 0;
        }
        return pageStart;
    }

    public Long getStartTime() {
        return Optional.ofNullable(startTime).orElse(0L);
    }

    public Long getEndTime() {
        return Optional.ofNullable(endTime).orElse(0L);
    }

    public Integer getType() {
        return Optional.ofNullable(type).orElse(0);
    }

}