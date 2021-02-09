package com.stackstech.honeybee.server.assets.service.impl;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.assets.dao.AssetsCatalogMapper;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.dao.DataRecyclerMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogElement;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.service.AssetsCatalogService;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.core.enums.types.AssetsCatalogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AssetsCatalogServiceImpl implements AssetsCatalogService {

    @Autowired
    private AssetsCatalogMapper catalogMapper;
    @Autowired
    private DataRecyclerMapper dataRecyclerMapper;
    @Autowired
    private AssetsModelMapper modelMapper;

    private List<Object> getModels(Long topicId) {
        List<Object> elements = null;

        Map<String, Object> map = Maps.newHashMap();
        map.put("assetsCatalogTopic", topicId);
        List<AssetsModelEntity> models = modelMapper.selectByParameter(map);
        if (models != null) {
            elements = Lists.newArrayList();
            for (AssetsModelEntity model : models) {
                elements.add(new AssetsCatalogElement(model.getId(), model.getAssetsModelName()));
            }
        }
        return elements;
    }

    @Override
    public boolean addAssetsCatalog(AssetsCatalogVo vo, Long ownerId) {
        AssetsCatalogEntity entity = new AssetsCatalogEntity().build(ownerId).copy(vo);
        //get record order number
        Map<String, Object> map = Maps.newHashMap();
        map.put("catalogParentId", entity.getCatalogParentId());
        map.put("catalogType", entity.getCatalogType().getCode());
        int order = catalogMapper.selectMaxOrder(map);
        entity.setCatalogOrder(order);
        return catalogMapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean updateAssetsCatalog(AssetsCatalogVo vo, Long ownerId) {
        AssetsCatalogEntity entity = new AssetsCatalogEntity().update(ownerId).copy(vo);
        return catalogMapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean deleteAssetsCatalog(Long recordId, Long ownerId) {
        return catalogMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsCatalogEntity getAssetsCatalog(Long recordId) {
        return catalogMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<AssetsCatalogEntity> getAssetsCatalogs(Map<String, Object> parameter) {
        return catalogMapper.selectByParameter(parameter);
    }

    @Override
    public List<AssetsCatalogElement> getAssetsCatalogList() {
        //get assets domains
        Map<String, Object> param = Maps.newHashMap();
        param.put("catalogType", AssetsCatalogType.DOMAIN.getCode());
        param.put("isroot", 0);
        List<AssetsCatalogEntity> domains = catalogMapper.selectByParameter(param);
        if (domains == null || domains.size() == 0) {
            return Collections.emptyList();
        }

        List<AssetsCatalogElement> elements = Lists.newArrayList();
        for (AssetsCatalogEntity domain : domains) {
            AssetsCatalogElement e = new AssetsCatalogElement(domain.getId(), domain.getCatalogName());
            // get assets topics by domain
            Map<String, Object> map = Maps.newHashMap();
            map.put("catalogType", AssetsCatalogType.TOPIC.getCode());
            map.put("catalogParentId", domain.getId());
            List<AssetsCatalogEntity> topics = catalogMapper.selectByParameter(map);
            if (topics != null) {
                List<Object> element = Lists.newArrayList();
                for (AssetsCatalogEntity topic : topics) {
                    AssetsCatalogElement t = new AssetsCatalogElement(topic.getId(), topic.getCatalogName());
                    t.setElement(getModels(topic.getId()));
                    element.add(t);
                }
                e.setElement(element);
            }
            // add element
            elements.add(e);
        }
        return elements;
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
