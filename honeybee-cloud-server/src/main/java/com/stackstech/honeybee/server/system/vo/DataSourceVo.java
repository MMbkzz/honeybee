package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NotNull(message = "data source parameter cannot be null")
public class DataSourceVo {

    private Long id;

    @NotNull(message = "data source name cannot be null")
    private String datasourceName;

    @NotNull(message = "data source type cannot be null")
    private String datasourceType;

    private Map<String, Object> datasourceParameters;

    private Integer status;

    private String desc;
}
