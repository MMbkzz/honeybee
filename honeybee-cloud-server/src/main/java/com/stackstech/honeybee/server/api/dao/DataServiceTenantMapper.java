package com.stackstech.honeybee.server.api.dao;

import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataServiceTenantMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceTenantEntity record);

    DataServiceTenantEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceTenantEntity record);

    List<DataServiceTenantEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}