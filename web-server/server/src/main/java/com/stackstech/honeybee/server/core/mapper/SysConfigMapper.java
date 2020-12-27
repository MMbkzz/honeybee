package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;

public interface SysConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysConfigEntity record);

    int insertSelective(SysConfigEntity record);

    SysConfigEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfigEntity record);

    int updateByPrimaryKey(SysConfigEntity record);
}