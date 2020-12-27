package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysMessageEntity record);

    int insertSelective(SysMessageEntity record);

    SysMessageEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysMessageEntity record);

    int updateByPrimaryKey(SysMessageEntity record);
}