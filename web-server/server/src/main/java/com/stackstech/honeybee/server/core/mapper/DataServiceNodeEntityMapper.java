package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceNodeEntity;

public interface DataServiceNodeEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceNodeEntity record);

    int insertSelective(DataServiceNodeEntity record);

    DataServiceNodeEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceNodeEntity record);

    int updateByPrimaryKey(DataServiceNodeEntity record);
}