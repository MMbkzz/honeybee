package com.stackstech.honeybee.server.api.vo;

import com.stackstech.honeybee.common.entity.DataServiceMeta;
import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DataServiceVo {

    @NotNull(message = "invalid data service id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "data service name cannot be empty", groups = {AddGroup.class})
    private String dataServiceName;

    @Min(value = 1L, message = "invalid assets model id", groups = {AddGroup.class})
    private Long assetsModelId;

    private List<DataServiceMeta> dataServiceMetas;

    private Integer cacheExpire;

    private Integer status;

    private String desc;

}
