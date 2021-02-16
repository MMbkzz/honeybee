package com.stackstech.honeybee.server.api.service.impl;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.common.entity.DataServiceMeta;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.api.dao.DataServiceMapper;
import com.stackstech.honeybee.server.api.entity.DataServiceElement;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.api.service.DataService;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean add(DataServiceEntity entity) throws ServerException, DataNotFoundException {
        setServiceMeta(entity);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceEntity entity) throws ServerException, DataNotFoundException {
        setServiceMeta(entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) throws ServerException, DataNotFoundException {
        return mapper.deleteByPrimaryKey(recordId, ownerId) > 0;
    }

    @Override
    public DataServiceEntity getSingle(Long recordId) throws ServerException, DataNotFoundException {
        DataServiceEntity service = mapper.selectByPrimaryKey(recordId);
        CommonUtil.isNull(service, "data service not found");
        AssetsModelEntity model = modelMapper.selectByPrimaryKey(service.getAssetsModelId());
        CommonUtil.isNull(model, "assets model not found");
        service.setAssetsCatalogDomain(model.getAssetsCatalogDomain());
        service.setAssetsCatalogDomainName(model.getAssetsCatalogDomainName());
        service.setAssetsCatalogTopic(model.getAssetsCatalogTopic());
        service.setAssetsCatalogTopicName(model.getAssetsCatalogTopicName());
        service.setDatasourceMeta(model.getDatasourceMeta());
        return service;
    }

    @Override
    public List<DataServiceEntity> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException {
        List<DataServiceEntity> entities = mapper.selectByParameter(parameter);
        CommonUtil.isEmpty(entities);
        return entities;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) throws ServerException {
        return mapper.selectTotalCount(parameter);
    }

    @Override
    public List<DataServiceElement> getDataServiceList() throws ServerException, DataNotFoundException {
        List<DataServiceEntity> entities = mapper.selectByParameter(null);
        CommonUtil.isEmpty(entities);
        List<DataServiceElement> elements = Lists.newArrayList();
        for (DataServiceEntity entity : entities) {
            elements.add(new DataServiceElement(entity.getId(), entity.getDataServiceName()));
        }
        return elements;
    }
}
