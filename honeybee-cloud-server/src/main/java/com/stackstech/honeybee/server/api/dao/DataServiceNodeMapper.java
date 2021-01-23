package com.stackstech.honeybee.server.api.dao;

import com.stackstech.honeybee.server.api.entity.DataServiceNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceNodeMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceNodeEntity record);

    DataServiceNodeEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceNodeEntity record);

}