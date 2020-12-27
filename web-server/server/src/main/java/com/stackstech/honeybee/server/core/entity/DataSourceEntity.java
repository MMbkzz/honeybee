package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataSourceEntity {
    private Long id;

    private String datasourceName;

    private String datasourceCode;

    private String datasourceConfig;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}