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
public class DataSourceEntity extends DataEntity {
    private Long id;

    private String datasourceName;

    @ApiModelProperty(hidden = true)
    private String datasourceCode;

    private String datasourceType;

    private String datasourceConfig;

    private String desc;

}