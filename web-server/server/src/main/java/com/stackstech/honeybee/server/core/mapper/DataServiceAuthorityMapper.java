package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceAuthority;

public interface DataServiceAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataServiceAuthority record);

    int insertSelective(DataServiceAuthority record);

    DataServiceAuthority selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceAuthority record);

    int updateByPrimaryKey(DataServiceAuthority record);
}