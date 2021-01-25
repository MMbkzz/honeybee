package com.stackstech.honeybee.server.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "tenant parameter cannot be null")
public class DataServiceTenantVo {

    private Long id;

    @NotNull(message = "tenant name cannot be null")
    private String tenantName;

    private Integer status;

    private String desc;
}
