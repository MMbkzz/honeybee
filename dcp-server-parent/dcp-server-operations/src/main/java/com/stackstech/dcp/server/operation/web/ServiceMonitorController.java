package com.stackstech.dcp.server.operation.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.operation.api.ApiUrls;
import com.stackstech.dcp.server.operation.service.ServiceMonitorService;
import com.stackstech.dcp.server.operation.vo.ServiceMonitorQueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务监控Controller
 */
@RestController
@RequestMapping(ApiUrls.API_BASE_URI)
public class ServiceMonitorController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMonitorController.class);

    @Autowired
    private ServiceMonitorService serviceMonitorService;

    /**
     * 服务监控查询
     *
     * @param map
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_MONITOR_QUERY_URI)
    public ResponseEntity<?> queryService(@RequestParam Map<String, Object> map) {
        Map<String, Object> serviceMap = null;
        try {
            serviceMap = serviceMonitorService.queryAll(map);
        } catch (Exception e) {
            logger.error("获取服务监控指标失败", e);
            return ResponseError.create(500, "获取服务监控指标失败");
        }
        return ResponseOk.create(serviceMap);
    }

    /**
     * 分页查询服务访问数量排名
     *
     * @param monitorQueryVO
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_MONITOR_QUERY_PAGE_URI)
    public ResponseEntity<?> queryPageAccess(ServiceMonitorQueryVO monitorQueryVO) {
        Map<String, Object> serviceMap = new HashMap<>();
        try {
            if (monitorQueryVO != null) {
                if (monitorQueryVO.getAccessType() != null && "accessCount".equalsIgnoreCase(monitorQueryVO.getAccessType())) {
                    serviceMap = serviceMonitorService.queryAccessCount(monitorQueryVO);
                } else if (monitorQueryVO.getAccessType() != null && "appCount".equalsIgnoreCase(monitorQueryVO.getAccessType())) {
                    serviceMap = serviceMonitorService.queryAppCount(monitorQueryVO);
                } else if (monitorQueryVO.getAccessType() != null && "accessAvg".equalsIgnoreCase(monitorQueryVO.getAccessType())) {
                    serviceMap = serviceMonitorService.queryAccessAvg(monitorQueryVO);
                } else if (monitorQueryVO.getAccessType() != null && "executeAvg".equalsIgnoreCase(monitorQueryVO.getAccessType())) {
                    serviceMap = serviceMonitorService.queryExecuteAvg(monitorQueryVO);
                }
            }
        } catch (Exception e) {
            logger.error("获取服务监控指标失败", e);
            return ResponseError.create(500, "获取服务监控指标失败");
        }
        return ResponseOk.create(serviceMap);
    }

}
