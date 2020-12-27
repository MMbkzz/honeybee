package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceEntity record);

    int insertSelective(DataServiceEntity record);

    DataServiceEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceEntity record);

    int updateByPrimaryKey(DataServiceEntity record);
}