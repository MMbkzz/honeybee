package com.stackstech.honeybee.server.assets.service;

import com.stackstech.honeybee.server.assets.entity.AssetsCatalogElement;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;

import java.util.List;
import java.util.Map;

public interface AssetsCatalogService {

    boolean addAssetsCatalog(AssetsCatalogVo vo, Long ownerId) throws ServerException;

    boolean updateAssetsCatalog(AssetsCatalogVo vo, Long ownerId) throws ServerException;

    boolean deleteAssetsCatalog(Long recordId, Long ownerId) throws ServerException;

    AssetsCatalogEntity getAssetsCatalog(Long recordId) throws ServerException, DataNotFoundException;

    List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter) throws ServerException, DataNotFoundException;

    List<AssetsCatalogElement> getAssetsCatalogList() throws ServerException, DataNotFoundException;

    boolean deleteDataRecycler(Long recordId, Long ownerId) throws ServerException;

    DataRecyclerEntity getDataRecycler(Long recordId) throws ServerException, DataNotFoundException;

    List<DataRecyclerEntity> getDataRecyclers(Map<String, Object> parameter) throws ServerException, DataNotFoundException;

    Integer getDataRecyclerCount(Map<String, Object> parameter) throws ServerException;


}
