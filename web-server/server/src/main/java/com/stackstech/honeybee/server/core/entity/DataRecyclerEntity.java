package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataRecyclerEntity extends DataEntity<DataRecyclerEntity> {
    private Long id;

    private Long assetsModelId;

    private String assetsModelName;

    private Long assetsCatalogDomain;

    private String catalogName;

    private Long assetsDataSize;

    private Long assetsDataCount;

    private String desc;

}