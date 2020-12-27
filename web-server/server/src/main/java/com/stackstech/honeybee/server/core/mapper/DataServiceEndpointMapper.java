package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceEndpointEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceEndpointMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceEndpointEntity record);

    int insertSelective(DataServiceEndpointEntity record);

    DataServiceEndpointEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceEndpointEntity record);

    int updateByPrimaryKey(DataServiceEndpointEntity record);
}