package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.dao.DataServiceTenantMapper;
import com.stackstech.honeybee.server.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TenantServiceImpl implements DataService<DataServiceTenantEntity> {

    @Autowired
    private DataServiceTenantMapper mapper;

    @Override
    public boolean add(DataServiceTenantEntity entity, Long ownerId) {
        entity.create(ownerId);
        entity.setTenantCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceTenantEntity entity, Long ownerId) {
        entity.update(ownerId);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataServiceTenantEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataServiceTenantEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}