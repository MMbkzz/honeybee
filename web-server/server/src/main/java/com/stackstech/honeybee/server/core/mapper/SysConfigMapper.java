package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysConfigMapper {

    int insertSelective(SysConfigEntity record);

    SysConfigEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfigEntity record);

}