package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceTenantEntity extends DataEntity<DataServiceTenantEntity> {
    private Long id;

    private String tenantName;

    private String tenantCode;

    private String desc;

}