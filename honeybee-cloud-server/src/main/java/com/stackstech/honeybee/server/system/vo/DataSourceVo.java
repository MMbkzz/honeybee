package com.stackstech.honeybee.server.system.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class DataSourceVo {

    @NotNull(message = "invalid data source id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "data source name cannot be null", groups = {AddGroup.class})
    private String datasourceName;

    @NotBlank(message = "data source type cannot be null", groups = {AddGroup.class})
    private String datasourceType;

    @NotNull(message = "data source parameter be null", groups = {AddGroup.class})
    private Map<String, Object> datasourceParameters;

    private Integer status;

    private String desc;
}
