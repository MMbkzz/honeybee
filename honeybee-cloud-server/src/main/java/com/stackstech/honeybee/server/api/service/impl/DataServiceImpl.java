package com.stackstech.honeybee.server.api.service.impl;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.server.api.dao.DataServiceMapper;
import com.stackstech.honeybee.server.api.entity.DataServiceElement;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceMeta;
import com.stackstech.honeybee.server.api.service.DataService;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
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
public class DataServiceImpl implements DataService {

    @Autowired
    private DataServiceMapper mapper;
    @Autowired
    private AssetsModelMapper modelMapper;

    private void setServiceMeta(DataServiceEntity entity) {
        List<DataServiceMeta> metaList = entity.getDataServiceVo().getDataServiceMetas();

        if (metaList != null && metaList.size() > 0) {
            JsonParameterList parameterList = new JsonParameterList();
            parameterList.addAll(metaList);
            entity.setServiceMeta(parameterList);
            //TODO 重新生成表达式SQL
            entity.setExpression("expression test...");
        }
    }

    @Override
    public boolean add(DataServiceEntity entity) {
        setServiceMeta(entity);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceEntity entity) {
        setServiceMeta(entity);
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


    @Override
    public List<DataServiceElement> getDataServiceList() {
        List<DataServiceEntity> entities = mapper.selectByParameter(null);
        if (entities != null && entities.size() > 0) {
            List<DataServiceElement> elements = Lists.newArrayList();
            for (DataServiceEntity entity : entities) {
                elements.add(new DataServiceElement(entity.getId(), entity.getDataServiceName()));
            }
            return elements;
        }
        return null;
    }
}
