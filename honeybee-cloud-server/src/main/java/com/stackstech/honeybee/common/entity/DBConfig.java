package com.stackstech.honeybee.common.entity;

import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBConfig {

    private DataSourceType dataSourceType;
    private String version;
    private String connector;
    private Object config;

}
