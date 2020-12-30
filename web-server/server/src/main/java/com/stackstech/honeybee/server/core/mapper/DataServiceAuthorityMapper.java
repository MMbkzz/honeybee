package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.DataServiceAuthorityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataServiceAuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(DataServiceAuthorityEntity record);

    DataServiceAuthorityEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataServiceAuthorityEntity record);

}