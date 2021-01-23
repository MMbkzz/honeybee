package com.stackstech.honeybee.server.assets.dao;

import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AssetsModelMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AssetsModelEntity record);

    AssetsModelEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssetsModelEntity record);

    List<AssetsModelEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}