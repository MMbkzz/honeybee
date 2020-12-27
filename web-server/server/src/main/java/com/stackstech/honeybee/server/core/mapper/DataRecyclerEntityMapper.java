package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataRecyclerEntity;

public interface DataRecyclerEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataRecyclerEntity record);

    int insertSelective(DataRecyclerEntity record);

    DataRecyclerEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataRecyclerEntity record);

    int updateByPrimaryKey(DataRecyclerEntity record);
}