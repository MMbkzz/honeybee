package com.stackstech.honeybee.common.entity;


import com.stackstech.honeybee.server.core.enums.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 请求的参数实体
 *
 * @author William
 */
@Deprecated
public class RequestParameter extends RequestParameterMap<String, Object> {

    public static final String PAGE_START = "pageStart";
    public static final String PAGE_SIZE = "pageSize";
    public static final String KEYWORDS = "keywords";
    public static final String ORDER = "order";
    public static final String ORDER_FIELD = "orderField";
    public static final String ORDER_TYPE = "orderType";
    public static final String STATUS = "status";

    /**
     * query record limit start index, by default PageStart is 0.
     *
     * @return Integer
     */
    public Integer getPageStart() {
        Integer pageStart = getInteger(PAGE_START);
        Integer pageSize = getPageSize();

        if (pageStart != null && pageStart > 1) {
            pageStart = (pageStart - 1) * pageSize;
        } else {
            pageStart = 0;
        }
        return pageStart;
    }

    /**
     * query record limit size, by default PageSize is 10.
     *
     * @return Integer
     */
    public Integer getPageSize() {
        return Optional.ofNullable(getInteger(PAGE_SIZE)).orElse(10);
    }


    /**
     * query by order filed name, by default sort type is `desc`
     *
     * @return String
     * @see import com.stackstech.honeybee.server.core.enums.Constant.SORTS
     */
    public String getOrder() {
        String orders = null;
        if (Constant.SORTS.contains(getString(ORDER_FIELD))) {
            if (Optional.ofNullable(getBoolean(ORDER_TYPE)).orElse(false)) {
                orders = StringUtils.join("`", getString(ORDER_FIELD), "`", " ASC");
            } else {
                orders = StringUtils.join("`", getString(ORDER_FIELD), "`", " DESC");
            }
        }
        return orders;
    }

    /**
     * query by keyword
     *
     * @return String
     */
    public String getKeywords() {
        if (StringUtils.isNotEmpty(getString(KEYWORDS))) {
            return StringUtils.join("%", getString(KEYWORDS).trim(), "%");
        }
        return null;
    }

    /**
     * query by status
     *
     * @return Integer
     */
    public Integer getStatus() {
        return getInteger(STATUS);
    }


    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> p = super.getParameter();
        p.put(PAGE_START, getPageStart());
        p.put(PAGE_SIZE, getPageSize());
        p.put(ORDER, getOrder());
        p.put(KEYWORDS, getKeywords());
        p.put(STATUS, getStatus());
        return p;
    }

    @Override
    public String toString() {
        return getParameter().toString();
    }
}