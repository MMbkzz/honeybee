package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsModelEntity extends DataEntity<AssetsModelEntity> {
    private Long id;

    private String assetsModelName;

    private String assetsModelCode;

    private Long assetsCatalogDomain;

    private Long assetsCatalogTopic;

    private Long datasourceId;

    private String datasourceMeta;

    private String expression;

    private String desc;

}