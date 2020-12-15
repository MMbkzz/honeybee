package com.stackstech.dcp.server.operations.dao;

import com.stackstech.dcp.server.operations.model.SysAuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SysAuditLogMapper {

    /**
     * 新增平台操作日志表
     *
     * @param sysAuditLog
     * @return
     */
    int insert(SysAuditLog sysAuditLog);

    /**
     * 平台操作日志表查询
     *
     * @param map
     * @return
     */
    List<SysAuditLog> queryAll(Map<String, Object> map);

    /**
     * 平台操作日志表详情查询
     *
     * @param id
     * @return
     */
    SysAuditLog queryByPrimaryKey(@Param("id") Long id);

    /**
     * 获取记录
     *
     * @param map
     * @return
     */
    int countAll(Map<String, Object> map);
}
