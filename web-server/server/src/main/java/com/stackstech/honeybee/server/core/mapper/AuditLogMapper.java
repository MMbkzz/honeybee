package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuditLogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AuditLogEntity record);

    AuditLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuditLogEntity record);

}