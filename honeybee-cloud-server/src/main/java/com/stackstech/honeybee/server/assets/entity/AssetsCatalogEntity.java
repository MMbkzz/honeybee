package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsCatalogEntity extends AbstractDataEntity<AssetsCatalogEntity> {

    private Long id;

    private Integer isroot;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private String catalogType;

    private Integer catalogOrder;

    private String desc;

    @Override
    public AssetsCatalogEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AssetsCatalogEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AssetsCatalogEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }


}