package com.stackstech.honeybee.server.service;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.core.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.core.vo.AssetsCatalogVo;

import java.util.List;
import java.util.Map;

public interface AssetsCatalogService {

    boolean addAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId);

    boolean updateAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId);

    boolean deleteAssetsCatalog(String assetsCatalogType, Long recordId, Long ownerId);

    AssetsCatalogEntity getAssetsCatalog(String assetsCatalogType, Long recordId);

    List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter);

    List<AssetsCatalogEntity> getAssetsCatalogTree(Map<String, Object> parameter);

    boolean deleteDataRecycler(Long recordId, Long ownerId);

    DataRecyclerEntity getDataRecycler(Long recordId);

    List<DataRecyclerEntity> getDataRecyclers(Map<String, Object> parameter);

    Integer getDataRecyclerCount(Map<String, Object> parameter);


}
