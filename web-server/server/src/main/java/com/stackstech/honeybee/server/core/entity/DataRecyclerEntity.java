package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataRecyclerEntity {
    private Long id;

    private Long assetsModelId;

    private String assetsModelName;

    private Long assetsCatalogDomain;

    private String catalogName;

    private Long assetsDataSize;

    private Long assetsDataCount;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}