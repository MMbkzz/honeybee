package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AssetsModelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AssetsModelEntity record);

    int insertSelective(AssetsModelEntity record);

    AssetsModelEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssetsModelEntity record);

    int updateByPrimaryKey(AssetsModelEntity record);
}