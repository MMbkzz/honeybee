package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class DataCacheVo {

    @NotBlank(message = "data cache uuid cannot be null")
    private String uuid;

    @Max(value = 72000, message = "invalid data cache expire number, max number is 72000")
    @Min(value = -1, message = "invalid data cache expire number, min number is -1")
    private int expire;
}
