package com.stackstech.honeybee.server.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NotNull(message = "data cache parameter cannot be null")
public class DataCacheQuery {

    @ApiModelProperty(required = true)
    @Min(value = 1, message = "invalid page start index, default value is 1")
    private int pageStart;

    @ApiModelProperty(required = true)
    @Size(min = 1, max = 100, message = "invalid page limit size, max limit size is 1~100")
    private int pageSize;

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
