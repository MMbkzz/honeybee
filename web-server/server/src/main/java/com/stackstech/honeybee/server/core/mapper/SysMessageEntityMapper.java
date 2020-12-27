package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysMessageEntity;

public interface SysMessageEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysMessageEntity record);

    int insertSelective(SysMessageEntity record);

    SysMessageEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysMessageEntity record);

    int updateByPrimaryKey(SysMessageEntity record);
}