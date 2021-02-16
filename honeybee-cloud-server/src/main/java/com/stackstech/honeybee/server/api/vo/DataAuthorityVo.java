package com.stackstech.honeybee.server.api.vo;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class DataAuthorityVo {

    @Min(value = 1L, message = "invalid data id")
    private Long id;

    @Min(value = 1L, message = "invalid data service id")
    private Long dataServiceId;

}
