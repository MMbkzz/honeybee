package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class DataServiceTenantEntity extends DataEntity {
    private Long id;

    private String tenantName;

    private String tenantCode;

    private String desc;

}