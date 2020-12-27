package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceTenantEntity;

public interface DataServiceTenantMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceTenantEntity record);

    int insertSelective(DataServiceTenantEntity record);

    DataServiceTenantEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceTenantEntity record);

    int updateByPrimaryKey(DataServiceTenantEntity record);
}