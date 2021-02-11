package com.stackstech.honeybee.server.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class DataCacheQuery {

    private String keywords;

    @Max(value = 10000, message = "invalid page index, max value is 10000")
    @ApiModelProperty(required = true)
    private Integer pageStart;

    @Min(value = 1, message = "invalid page limit size, min value is 1")
    @Max(value = 100, message = "invalid page limit size, max value is 100")
    @ApiModelProperty(required = true)
    private Integer pageSize;

    public int getPageStart() {
        if (pageStart <= 0) {
            pageStart = 1;
        }
        return pageStart;
    }

    public int getPageSize() {
        if (pageSize <= 0) {
            pageSize = 1;
        }
        return pageSize;
    }
}
