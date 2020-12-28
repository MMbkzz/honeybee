package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AssetsCatalogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AssetsCatalogEntity record);

    AssetsCatalogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssetsCatalogEntity record);

}