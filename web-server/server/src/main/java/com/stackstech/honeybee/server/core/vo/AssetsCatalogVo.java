package com.stackstech.honeybee.server.core.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "assets catalog parameter cannot be null")
public class AssetsCatalogVo {


    private Long id;

    @NotNull(message = "assets catalog name cannot be null")
    private String catalogName;

    @Min(value = 1L, message = "invalid assets catalog id")
    private Long catalogParentId;

    @Min(value = 1L, message = "invalid catalog order number")
    private Integer catalogOrder;

    private String desc;
}
