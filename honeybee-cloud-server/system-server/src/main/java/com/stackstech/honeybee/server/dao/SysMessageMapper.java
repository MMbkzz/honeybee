package com.stackstech.honeybee.server.dao;

import com.stackstech.honeybee.server.core.entity.SysMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(SysMessageEntity record);

    SysMessageEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysMessageEntity record);

}