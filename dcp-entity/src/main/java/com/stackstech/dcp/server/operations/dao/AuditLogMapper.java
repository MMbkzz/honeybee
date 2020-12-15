package com.stackstech.dcp.server.operations.dao;

import com.stackstech.dcp.server.operations.model.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AuditLogMapper {

    /**
     * 新增服务审计日志表
     *
     * @param auditLog
     * @return
     */
    int insert(AuditLog auditLog);

    /**
     * 服务审计日志表查询
     *
     * @param map
     * @return
     */
    List<AuditLog> queryAll(Map<String, Object> map);

    /**
     * 服务审计日志表详情查询
     *
     * @param id
     * @return
     */
    AuditLog queryByPrimaryKey(@Param("id") Integer id);

    /**
     * 获取记录
     *
     * @param map
     * @return
     */
    int countAll(Map<String, Object> map);
}
