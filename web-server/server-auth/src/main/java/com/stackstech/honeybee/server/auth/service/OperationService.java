package com.stackstech.honeybee.server.auth.service;

import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.server.auth.model.AuthOperation;

import java.util.Map;

/**
 * 操作
 */
public interface OperationService {
    /**
     * 新增操作
     *
     * @param record
     * @return
     */
    int insert(AuthOperation record, Long UserId);

    /**
     * 删除操作
     *
     * @param record
     * @return
     */
    int deleteByPrimaryKey(AuthOperation record);

    /**
     * 修改操作
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AuthOperation record, Long UserId);

    /**
     * 获取操作
     *
     * @param record
     * @return
     */
    Map<String, Object> getOperations(AuthOperation record, Page page);

}
