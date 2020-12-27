package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;

public interface AssetsCatalogEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AssetsCatalogEntity record);

    int insertSelective(AssetsCatalogEntity record);

    AssetsCatalogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssetsCatalogEntity record);

    int updateByPrimaryKey(AssetsCatalogEntity record);
}