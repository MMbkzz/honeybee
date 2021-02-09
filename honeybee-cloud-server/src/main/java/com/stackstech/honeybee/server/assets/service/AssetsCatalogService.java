package com.stackstech.honeybee.server.assets.service;

import com.stackstech.honeybee.server.assets.entity.AssetsCatalogElement;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;

import java.util.List;
import java.util.Map;

public interface AssetsCatalogService {

    boolean addAssetsCatalog(AssetsCatalogVo vo, Long ownerId);

    boolean updateAssetsCatalog(AssetsCatalogVo vo, Long ownerId);

    boolean deleteAssetsCatalog(Long recordId, Long ownerId);

    AssetsCatalogEntity getAssetsCatalog(Long recordId);

    List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter);

    List<AssetsCatalogElement> getAssetsCatalogList();

    boolean deleteDataRecycler(Long recordId, Long ownerId);

    DataRecyclerEntity getDataRecycler(Long recordId);

    List<DataRecyclerEntity> getDataRecyclers(Map<String, Object> parameter);

    Integer getDataRecyclerCount(Map<String, Object> parameter);


}
