package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysConfigEntity record);

    int insertSelective(SysConfigEntity record);

    SysConfigEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfigEntity record);

    int updateByPrimaryKey(SysConfigEntity record);
}