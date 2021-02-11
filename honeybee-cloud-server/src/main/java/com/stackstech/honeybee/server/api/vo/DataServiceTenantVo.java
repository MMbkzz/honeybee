package com.stackstech.honeybee.server.api.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DataServiceTenantVo {

    @NotNull(message = "invalid data tenant id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "tenant name cannot be null", groups = {AddGroup.class})
    private String tenantName;

    private Integer status;

    private String desc;
}
