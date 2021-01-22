package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class DataServiceTenantEntity extends DataEntity {
    private Long id;

    private String tenantName;

    @ApiModelProperty(hidden = true)
    private String tenantCode;

    private String desc;

}