package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsCatalogEntity extends DataEntity<AssetsCatalogEntity> {

    private Long id;

    private Integer isroot;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private String catalogType;

    private Integer catalogOrder;

    private String desc;

}