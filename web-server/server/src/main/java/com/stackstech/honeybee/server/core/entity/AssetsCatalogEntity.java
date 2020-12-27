package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsCatalogEntity {
    private Long id;

    private Integer isroot;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private String catalogType;

    private Integer catalogOrder;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}