package com.stackstech.honeybee.server.assets.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AssetsCatalogVo {

    @NotNull(message = "invalid data catalog id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "assets catalog name cannot be null", groups = {AddGroup.class})
    private String catalogName;

    @Min(value = 1L, message = "invalid assets catalog id", groups = {AddGroup.class})
    private Long catalogParentId;

    private String catalogType;

    private Integer status;

    private String desc;
}
