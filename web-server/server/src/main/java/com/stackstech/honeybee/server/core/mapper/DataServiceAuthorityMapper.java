package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceAuthority record);

    DataServiceAuthority selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceAuthority record);

}