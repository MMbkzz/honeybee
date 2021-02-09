package com.stackstech.honeybee.server.api.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.api.dao.DataServiceAuthorityMapper;
import com.stackstech.honeybee.server.api.dao.DataServiceMapper;
import com.stackstech.honeybee.server.api.dao.DataServiceTenantMapper;
import com.stackstech.honeybee.server.api.entity.DataAuthorityMeta;
import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.api.service.TenantService;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private DataServiceTenantMapper tenantMapper;
    @Autowired
    private DataServiceAuthorityMapper authorityMapper;
    @Autowired
    private DataServiceMapper serviceMapper;
    @Autowired
    private AssetsModelMapper modelMapper;

    @Override
    public boolean add(DataServiceTenantEntity entity) {
        return tenantMapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceTenantEntity entity) {
        return tenantMapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return tenantMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataServiceTenantEntity getSingle(Long recordId) {
        return tenantMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataServiceTenantEntity> get(Map<String, Object> parameter) {
        return tenantMapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return tenantMapper.selectTotalCount(parameter);
    }

    @Override
    public List<DataServiceAuthorityEntity> getAuthorityList(Long tenantId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("tenantId", tenantId);
        return authorityMapper.selectByParameter(map);
    }

    @Override
    public List<DataAuthorityMeta> getDataAuthorityMeta(Long authorityId, Long dataServiceId) {
        List<DataAuthorityMeta> dataAuthorityMetaList = Lists.newArrayList();

        DataServiceAuthorityEntity entity = authorityMapper.selectByPrimaryKey(authorityId);
        if (entity.getAuthorityData() != null && entity.getAuthorityData().size() > 0) {
            return (List<DataAuthorityMeta>) entity.getAuthorityData();
        }
        //if authority data is empty then get data model meta
        DataServiceEntity service = serviceMapper.selectByPrimaryKey(dataServiceId);
        if (service != null) {
            AssetsModelEntity model = modelMapper.selectByPrimaryKey(service.getAssetsModelId());
            Assert.notNull(model, "Assets model not found");
            JsonParameterList datasourceMetas = model.getDatasourceMeta();
            for (Object meta : datasourceMetas) {
                Map<String, Object> map = (LinkedHashMap) meta;
                dataAuthorityMetaList.add(new DataAuthorityMeta(map.get("paramName").toString(), true));
            }
        }
        return dataAuthorityMetaList;
    }

    @Override
    public boolean updateDataAuthorityMeta(Long authorityId, List<DataAuthorityMeta> dataAuthorityMete, Long ownerId) {
        DataServiceAuthorityEntity entity = new DataServiceAuthorityEntity().update(ownerId);
        entity.setId(authorityId);
        JsonParameterList metas = new JsonParameterList<>();
        metas.addAll(dataAuthorityMete);
        entity.setAuthorityData(metas);
        return authorityMapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean deleteDataAuthority(Long authorityId, Long ownerId) {
        return authorityMapper.deleteByPrimaryKey(authorityId) > 0;
    }

    @Override
    public DataServiceAuthorityEntity addDataAuthority(Long tenantId, Long dataServiceId, Long ownerId) {
        DataServiceAuthorityEntity entity = new DataServiceAuthorityEntity().build(ownerId);
        entity.setTenantId(tenantId);
        entity.setDataServiceId(dataServiceId);
        entity.setAuthorityToken(CommonUtil.createAuthorityCode());
        entity.setAuthorityExpire(7200L);
        entity.setAuthorityData(new JsonParameterList());
        if (authorityMapper.insertSelective(entity) > 0) {
            return entity;
        }
        return null;
    }
}
