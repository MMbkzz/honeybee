package com.stackstech.honeybee.server.assets.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "assets model parameter cannot be null")
public class AssetsModelVo {

    private Long id;

    @NotNull(message = "assets model name cannot be null")
    private String assetsModelName;

    @Min(value = 1L, message = "invalid assets catalog domain id")
    private Long assetsCatalogDomain;

    @Min(value = 1L, message = "invalid assets catalog topic id")
    private Long assetsCatalogTopic;

    @Min(value = 1L, message = "invalid datasource id")
    private Long datasourceId;

    @NotNull(message = "assets model expression cannot be null")
    private String expression;

    private Integer status;

    private String desc;
}
