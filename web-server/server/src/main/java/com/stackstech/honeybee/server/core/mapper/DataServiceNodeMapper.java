package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceNodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceNodeEntity record);

    int insertSelective(DataServiceNodeEntity record);

    DataServiceNodeEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceNodeEntity record);

    int updateByPrimaryKey(DataServiceNodeEntity record);
}