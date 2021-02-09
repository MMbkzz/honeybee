package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.AssetsCatalogType;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsCatalogEntity extends AbstractDataEntity<AssetsCatalogEntity> {

    private Long id;

    private Integer isroot = 0;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private AssetsCatalogType catalogType;

    private String catalogTypeName;

    private Integer catalogOrder;

    private String desc;

    public String getCatalogTypeName() {
        return catalogType.getName();
    }

    @Override
    public AssetsCatalogEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
        this.catalogCode = CommonUtil.generateEntityCode();
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