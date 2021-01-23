package com.stackstech.honeybee.server.core.vo;

import com.stackstech.honeybee.server.api.entity.DataServiceParameter;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NotNull(message = "data service parameter cannot be null")
public class DataServiceVo {

    private Long id;

    @NotNull(message = "data service name cannot be null")
    private String dataServiceName;

    @Min(value = 1L, message = "invalid assets model id")
    private Long assetsModelId;

    private List<DataServiceParameter> dataServiceParameters;

    private Integer cacheExpire;

    private String desc;

}
