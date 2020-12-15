package com.stackstech.dcp.server.operation.service;

import com.stackstech.dcp.server.operation.vo.ServiceMonitorQueryVO;

import java.util.Map;

/**
 * 服务监控Service
 */
public interface ServiceMonitorService {

    Map<String, Object> queryAppCount(ServiceMonitorQueryVO monitorQueryVO) throws Exception;

    Map<String, Object> queryAccessAvg(ServiceMonitorQueryVO monitorQueryVO) throws Exception;

    Map<String, Object> queryExecuteAvg(ServiceMonitorQueryVO monitorQueryVO) throws Exception;

    Map<String, Object> queryAll(Map<String, Object> map) throws Exception;

    Map<String, Object> queryAccessCount(ServiceMonitorQueryVO monitorQueryVO) throws Exception;

}
