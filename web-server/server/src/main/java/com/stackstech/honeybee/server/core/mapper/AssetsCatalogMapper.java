package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AssetsCatalogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AssetsCatalogEntity record);

    AssetsCatalogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssetsCatalogEntity record);

    List<AssetsCatalogEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}