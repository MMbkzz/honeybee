package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataSourceMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataSourceEntity record);

    DataSourceEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataSourceEntity record);

    List<DataSourceEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);
}