package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataRecyclerEntity extends AbstractDataEntity<DataRecyclerEntity, Object> {
    private Long id;

    private Long assetsModelId;

    private String assetsModelName;

    private Long assetsCatalogDomain;

    private String catalogName;

    private Long assetsDataSize;

    private Long assetsDataCount;

    private String desc;

    @Override
    public DataRecyclerEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataRecyclerEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Deprecated
    @Override
    public DataRecyclerEntity build(Long ownerId, Object vo) {
        return null;
    }

    @Deprecated
    @Override
    public DataRecyclerEntity update(Long ownerId, Object vo) {
        return null;
    }
}