package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsModelEntity extends AbstractDataEntity<AssetsModelEntity> {
    private Long id;

    private String assetsModelName;

    private String assetsModelCode;

    private Long assetsCatalogDomain;

    private Long assetsCatalogTopic;

    private Long datasourceId;

    private JsonParameterMap datasourceMeta;

    private String expression;

    private String desc;

    private String assetsCatalogDomainName;

    private String assetsCatalogTopicName;

    @Override
    public AssetsModelEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.assetsModelCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public AssetsModelEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AssetsModelEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}