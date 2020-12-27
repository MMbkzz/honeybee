package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataServiceNodeEntity {
    private Long id;

    private String serviceNodeName;

    private String serviceNodeCode;

    private String serviceNodeIp;

    private String serviceNodePort;

    private String serviceNodeEndpoint;

    private String serviceNodeStatus;

    private Date updatetime;

    private Date createtime;

    private String desc;

}