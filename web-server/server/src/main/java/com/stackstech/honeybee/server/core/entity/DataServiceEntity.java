package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEntity {
    private Long id;

    private String dataServiceName;

    private String dataServiceCode;

    private Long assetsModelId;

    private String datasourceMeta;

    private String serviceMeta;

    private Integer cacheExpire;

    private String expression;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}