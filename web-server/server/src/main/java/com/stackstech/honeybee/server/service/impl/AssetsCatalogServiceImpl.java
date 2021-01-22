package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.core.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.core.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.service.AssetsCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AssetsCatalogServiceImpl implements AssetsCatalogService {

    @Override
    public boolean addAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId) {
        return false;
    }

    @Override
    public boolean updateAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId) {
        return false;
    }

    @Override
    public boolean deleteAssetsCatalog(String assetsCatalogType, Long recordId, Long ownerId) {
        return false;
    }

    @Override
    public AssetsCatalogEntity getAssetsCatalog(String assetsCatalogType, Long recordId) {
        return null;
    }

    @Override
    public List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AssetsCatalogEntity> getAssetsCatalogTree(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public boolean deleteDataRecycler(Long recordId, Long ownerId) {
        return false;
    }

    @Override
    public DataRecyclerEntity getDataRecycler(Long recordId) {
        return null;
    }

    @Override
    public List<DataRecyclerEntity> getDataRecyclers(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public Integer getDataRecyclerCount(Map<String, Object> parameter) {
        return null;
    }
}
