package com.stackstech.honeybee.server.api.service;

import com.stackstech.honeybee.server.api.dao.DataServiceMapper;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * DataServiceImpl
 *
 * @author william
 */
@Service
public class DataServiceImpl implements DataService<DataServiceEntity> {

    @Autowired
    private DataServiceMapper mapper;
    @Autowired
    private AssetsModelMapper modelMapper;

    @Override
    public boolean add(DataServiceEntity entity) {
        //TODO
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceEntity entity) {
        //TODO
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId, ownerId) > 0;
    }

    @Override
    public DataServiceEntity getSingle(Long recordId) {
        DataServiceEntity service = mapper.selectByPrimaryKey(recordId);
        if (service != null) {
            AssetsModelEntity model = modelMapper.selectByPrimaryKey(service.getAssetsModelId());
            Assert.notNull(model, "Assets model not found");
            service.setAssetsCatalogDomain(model.getAssetsCatalogDomain());
            service.setAssetsCatalogDomainName(model.getAssetsCatalogDomainName());
            service.setAssetsCatalogTopic(model.getAssetsCatalogTopic());
            service.setAssetsCatalogTopicName(model.getAssetsCatalogTopicName());
            service.setDatasourceMeta(model.getDatasourceMeta());
        }
        return service;
    }

    @Override
    public List<DataServiceEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }


}
