package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceEndpointEntity;

public interface DataServiceEndpointEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceEndpointEntity record);

    int insertSelective(DataServiceEndpointEntity record);

    DataServiceEndpointEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceEndpointEntity record);

    int updateByPrimaryKey(DataServiceEndpointEntity record);
}