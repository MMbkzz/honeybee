package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceEntity extends DataEntity<DataSourceEntity> {
    private Long id;

    private String datasourceName;

    private String datasourceCode;

    private String datasourceType;

    private String datasourceConfig;

    private String desc;

}