package com.stackstech.honeybee.server.assets.service.impl;

import com.stackstech.honeybee.server.assets.dao.AssetsCatalogMapper;
import com.stackstech.honeybee.server.assets.dao.DataRecyclerMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.service.AssetsCatalogService;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.core.enums.types.AssetsCatalogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AssetsCatalogServiceImpl implements AssetsCatalogService {

    @Autowired
    private AssetsCatalogMapper assetsCatalogMapper;
    @Autowired
    private DataRecyclerMapper dataRecyclerMapper;

    @Override
    public boolean addAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId) {
        AssetsCatalogEntity entity = new AssetsCatalogEntity().build(ownerId);
        if (assetsCatalogType.equalsIgnoreCase(AssetsCatalogType.DOMAIN.getName())) {
            entity.setCatalogType(AssetsCatalogType.DOMAIN);
        }
        if (assetsCatalogType.equalsIgnoreCase(AssetsCatalogType.TOPIC.getName())) {
            entity.setCatalogType(AssetsCatalogType.TOPIC);
        }
        return assetsCatalogMapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean updateAssetsCatalog(String assetsCatalogType, AssetsCatalogVo vo, Long ownerId) {
        AssetsCatalogEntity entity = new AssetsCatalogEntity().update(ownerId);
        if (assetsCatalogType.equalsIgnoreCase(AssetsCatalogType.DOMAIN.getName())) {
            entity.setCatalogType(AssetsCatalogType.DOMAIN);
        }
        if (assetsCatalogType.equalsIgnoreCase(AssetsCatalogType.TOPIC.getName())) {
            entity.setCatalogType(AssetsCatalogType.TOPIC);
        }
        return assetsCatalogMapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean deleteAssetsCatalog(Long recordId, Long ownerId) {
        return assetsCatalogMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsCatalogEntity getAssetsCatalog(Long recordId) {
        return assetsCatalogMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter) {
        return assetsCatalogMapper.selectByParameter(parameter);
    }

    @Override
    public List<AssetsCatalogEntity> getAssetsCatalogTree(Map<String, Object> parameter) {
        //TODO
        return null;
    }

    @Override
    public boolean deleteDataRecycler(Long recordId, Long ownerId) {
        return dataRecyclerMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataRecyclerEntity getDataRecycler(Long recordId) {
        return dataRecyclerMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataRecyclerEntity> getDataRecyclers(Map<String, Object> parameter) {
        return dataRecyclerMapper.selectByParameter(parameter);
    }

    @Override
    public Integer getDataRecyclerCount(Map<String, Object> parameter) {
        return dataRecyclerMapper.selectTotalCount(parameter);
    }
}
