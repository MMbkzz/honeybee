package com.stackstech.honeybee.server.audit.dao;

import com.stackstech.honeybee.server.audit.entity.AuditLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AuditLogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AuditLogEntity record);

    AuditLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuditLogEntity record);

    List<AuditLogEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

}