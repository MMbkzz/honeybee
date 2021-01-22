package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEndpointEntity extends DataEntity {
    private Long id;

    @ApiModelProperty(hidden = true)
    private String dataServiceEndpointCode;

    private Long serviceNodeId;

    private Long dataServiceId;

    private Integer dataServiceResource;

    private String dataServiceEndpoint;

    private String dataServiceStatus;

    private String desc;

}