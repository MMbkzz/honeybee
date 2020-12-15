package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.server.auth.model.AuthOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作
 * <p>
 * 表名 dcp_auth_resource_operation
 */
@Mapper
@Repository
public interface AuthOperationMapper {
    /**
     * 新增操作
     *
     * @param record
     * @return
     */
    int insert(AuthOperation record);

    int insertSelective(AuthOperation record);

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 修改操作
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AuthOperation record);

    /**
     * 获取操作
     *
     * @param record
     * @return
     */
    List<AuthOperation> getOperations(AuthOperation record);

}
