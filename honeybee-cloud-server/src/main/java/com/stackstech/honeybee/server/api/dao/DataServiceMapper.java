package com.stackstech.honeybee.server.api.dao;

import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataServiceMapper {
    int deleteByPrimaryKey(Long id, Long ownerId);

    int insertSelective(DataServiceEntity record);

    DataServiceEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceEntity record);

    List<DataServiceEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}