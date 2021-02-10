package com.stackstech.honeybee.common.entity;

import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import lombok.Data;

@Data
public class DBConfig {

    private DataSourceType dataSourceType;
    private String version;
    private String connector;
    private Object config;

}
