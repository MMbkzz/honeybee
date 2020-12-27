package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataServiceEndpointEntity {
    private Long id;

    private String dataServiceEndpointCode;

    private Long serviceNodeId;

    private Long dataServiceId;

    private Integer dataServiceResource;

    private String dataServiceEndpoint;

    private String dataServiceStatus;

    private Date updatetime;

    private Date createtime;

    private String desc;

}