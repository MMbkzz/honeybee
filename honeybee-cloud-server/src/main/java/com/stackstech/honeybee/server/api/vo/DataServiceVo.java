package com.stackstech.honeybee.server.api.vo;

import com.stackstech.honeybee.common.entity.DataServiceMeta;
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

    private List<DataServiceMeta> dataServiceMetas;

    private Integer cacheExpire;

    private Integer status;

    private String desc;

}
