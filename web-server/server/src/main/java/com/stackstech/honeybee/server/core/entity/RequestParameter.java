package com.stackstech.honeybee.server.core.entity;


import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
     * query by order filed name
     *
     * @see import com.stackstech.honeybee.server.core.enums.Constant.SORTS
     */
    private String orderField;
    /**
     * query by order type
     */
    private boolean orderType;
    /**
     * query by types
     */
    private String type;
    /**
     * query by status
     */
    private Integer status;


    public String getOrder() {
        String orders = null;
        if (Constant.SORTS.contains(orderField)) {
            if (orderType) {
                orders = StringUtils.join("`", orderField, "`", " ASC");
            } else {
                orders = StringUtils.join("`", orderField, "`", " DESC");
            }
        }
        return orders;
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

    public String getKeywords() {
        if (StringUtils.isNotEmpty(keywords)) {
            return StringUtils.join("%", keywords.trim(), "%");
        }
        return null;
    }

}