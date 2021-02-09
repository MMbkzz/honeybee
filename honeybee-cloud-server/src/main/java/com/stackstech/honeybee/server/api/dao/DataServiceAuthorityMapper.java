package com.stackstech.honeybee.server.api.dao;

import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataServiceAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceAuthorityEntity record);

    DataServiceAuthorityEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceAuthorityEntity record);

    List<DataServiceAuthorityEntity> selectByParameter(Map<String, Object> parameter);
}