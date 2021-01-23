package com.stackstech.honeybee.server.assets.dao;

import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataRecyclerMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataRecyclerEntity record);

    DataRecyclerEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataRecyclerEntity record);

    List<DataRecyclerEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}