package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceTenantEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceTenantMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceTenantEntity record);

    DataServiceTenantEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceTenantEntity record);

}