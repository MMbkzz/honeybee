package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceAuthorityEntity {
    private Long id;

    private Long tenantId;

    private Long dataServiceId;

    private String authorityToken;

    private Long authorityExpire;

    private String authorityData;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}