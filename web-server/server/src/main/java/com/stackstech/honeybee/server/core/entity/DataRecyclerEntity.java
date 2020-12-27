package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DataRecyclerEntity {
    private Long id;

    private Long assetsModelId;

    private Long assetsDataSize;

    private Long assetsDataCount;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}