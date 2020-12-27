package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataSourceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataSourceEntity record);

    int insertSelective(DataSourceEntity record);

    DataSourceEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataSourceEntity record);

    int updateByPrimaryKey(DataSourceEntity record);
}