package com.stackstech.honeybee.common.vo;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.enums.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.util.Map;

@Data
@ApiModel
@NotNull(message = "query parameter cannot be null")
public class PageQuery {

    public static final String PAGE_START = "pageStart";
    public static final String PAGE_SIZE = "pageSize";
    public static final String KEYWORDS = "keywords";
    public static final String ORDER = "order";
    public static final String ORDER_FIELD = "orderField";
    public static final String ORDER_TYPE = "orderType";
    public static final String STATUS = "status";

    @Min(value = 0L, message = "invalid page start index, default value is 0")
    @ApiModelProperty(required = true)
    private Integer pageStart;

    @Size(min = 1, max = 100, message = "invalid page limit size, max limit size is 1~100")
    @ApiModelProperty(required = true)
    private Integer pageSize;

    private String keywords;

    private String orderField;

    @AssertFalse(message = "invalid order type, must be `true` or `false`")
    @AssertTrue(message = "invalid order type, must be `true` or `false`")
    private boolean orderType;

    private Integer status;

    /**
     * query record limit start index, by default PageStart is 0.
     *
     * @return Integer
     */
    public Integer getPageStart() {
        if (pageStart > 1) {
            pageStart = (pageStart - 1) * pageSize;
        } else {
            pageStart = 0;
        }
        return pageStart;
    }


    /**
     * query by order filed name, by default sort type is `desc`
     *
     * @return String
     * @see import com.stackstech.honeybee.server.core.enums.Constant.SORTS
     */
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

    /**
     * query by keyword
     *
     * @return String
     */
    public String getKeywords() {
        if (StringUtils.isNotEmpty(keywords)) {
            return StringUtils.join("%", keywords.trim(), "%");
        }
        return null;
    }

    public Map<String, Object> getParameter() {
        Map<String, Object> p = Maps.newHashMap();
        p.put(PAGE_START, getPageStart());
        p.put(PAGE_SIZE, getPageSize());
        p.put(ORDER, getOrder());
        p.put(KEYWORDS, getKeywords());
        p.put(STATUS, getStatus());
        return p;
    }

}
