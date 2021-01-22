package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceNodeEntity extends DataEntity {
    private Long id;

    private String serviceNodeName;

    @ApiModelProperty(hidden = true)
    private String serviceNodeCode;

    private String serviceNodeIp;

    private String serviceNodePort;

    private String serviceNodeEndpoint;

    private String serviceNodeStatus;

    private String desc;

}