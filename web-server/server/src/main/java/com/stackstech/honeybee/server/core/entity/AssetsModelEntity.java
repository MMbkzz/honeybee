package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsModelEntity {
    private Long id;

    private String assetsModelName;

    private String assetsModelCode;

    private Long assetsCatalogDomain;

    private Long assetsCatalogTopic;

    private Long datasourceId;

    private String datasourceMeta;

    private String expression;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}