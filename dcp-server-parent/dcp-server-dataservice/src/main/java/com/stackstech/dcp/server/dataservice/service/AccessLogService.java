package com.stackstech.dcp.server.dataservice.service;

import com.stackstech.dcp.server.dataservice.model.AccessLog;
import com.stackstech.dcp.server.dataservice.vo.AccessLogQueryVO;
import com.stackstech.dcp.server.dataservice.vo.AccessLogVO;

import java.util.Map;

/**
 * 数据服务API访问日志接口定义
 */
public interface AccessLogService {

    /**
     * 数据服务API访问日志查询
     *
     * @param accessLogQueryVO
     * @return
     */
    Map<String, Object> queryAll(AccessLogQueryVO accessLogQueryVO) throws Exception;

    /**
     * 数据服务API访问日志详情查询
     *
     * @param id
     * @return
     */
    AccessLogVO query(Integer id) throws Exception;

    /**
     * 新增数据服务API访问日志
     *
     * @param accessLog
     * @return
     */
    int add(AccessLog accessLog) throws Exception;

    /**
     * 数据服务API访问日志删除
     *
     * @param id
     * @return
     */
    int delete(Integer id) throws Exception;

    /**
     * 数据服务API访问日志修改
     *
     * @param accessLog
     * @return
     */
    int update(AccessLog accessLog) throws Exception;

    int countAll(AccessLogQueryVO accessLogQueryVO) throws Exception;
}
