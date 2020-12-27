package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataServiceTenantEntity {
    private Long id;

    private String tenantName;

    private String tenantCode;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;
}