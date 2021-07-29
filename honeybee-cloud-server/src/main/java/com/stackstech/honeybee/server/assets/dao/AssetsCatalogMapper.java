package com.stackstech.honeybee.server.assets.dao;

import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
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
    //根据某个参数查询
    List<AssetsCatalogEntity> selectByParameter(Map<String, Object> parameter);
    //数量
    int selectTotalCount(Map<String, Object> parameter);
    //查询最大的
    int selectMaxOrder(Map<String, Object> parameter);

}