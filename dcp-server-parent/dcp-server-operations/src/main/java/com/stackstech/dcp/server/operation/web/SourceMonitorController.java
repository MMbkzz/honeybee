package com.stackstech.dcp.server.operation.web;


import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.operation.api.ApiUrls;
import com.stackstech.dcp.server.operation.service.SourceMonitorService;
import com.stackstech.dcp.server.operation.vo.InstanceVO;
import com.stackstech.dcp.server.operation.vo.SourceMonitorQueryVO;
import com.stackstech.dcp.server.operation.vo.SourceMonitorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源监控Controller
 */
@RestController
@RequestMapping(ApiUrls.API_BASE_URI)
public class SourceMonitorController {

    private static final Logger logger = LoggerFactory.getLogger(SourceMonitorController.class);

    @Autowired
    private SourceMonitorService sourceMonitorService;

    /**
     * 获取数据源监控列表
     *
     * @param queryVO
     * @return
     */
    @GetMapping(value = ApiUrls.API_SOURCE_MONITOR_QUERY_URI)
    public ResponseEntity<?> querySources(SourceMonitorQueryVO queryVO, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<SourceMonitorVO> list = sourceMonitorService.queryAll(queryVO);
            int count = sourceMonitorService.countAll(queryVO);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("查询数据源监控列表失败!!", e);
            return ResponseError.create(500, "查询数据源监控列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 获取实例监控列表
     *
     * @param queryVO
     * @return
     */
    @GetMapping(value = ApiUrls.API_INSTANCE_MONITOR_QUERY_URI)
    public ResponseEntity<?> queryInstances(SourceMonitorQueryVO queryVO, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<InstanceVO> list = sourceMonitorService.queryInstance(queryVO);
            int count = sourceMonitorService.countInstance(queryVO);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("查询实例监控列表失败!!", e);
            return ResponseError.create(500, "查询实例监控列表失败");
        }
        return ResponseOk.create(map);
    }
}
