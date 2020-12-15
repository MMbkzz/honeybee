package com.stackstech.dcp.server.operation.web;


import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.operation.api.ApiUrls;
import com.stackstech.dcp.server.operation.service.ClusterAuditService;
import com.stackstech.dcp.server.operation.vo.ClusterAuditQueryVO;
import com.stackstech.dcp.server.operations.model.AuditLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务审计日志Controller
 */
@RestController
@RequestMapping(ApiUrls.API_BASE_URI)
public class ClusterAuditController {
    private final Logger log = LoggerFactory.getLogger(ClusterAuditController.class);

    @Autowired
    private ClusterAuditService clusterAuditService;

    /**
     * 查询服务审计日志表
     *
     * @param auditLog
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUDIT_LOG_ADD_URI)
    public ResponseEntity<?> addDataClusterAudit(@RequestBody AuditLog auditLog) {
        try {
            int i = clusterAuditService.add(auditLog);
            if (i <= 0) {
                return ResponseError.create(500, "新增服务审计日志失败");
            }
        } catch (Exception e) {
            log.error("新增服务审计日志失败", e);
            return ResponseError.create(500, "新增服务审计日志失败");
        }
        return ResponseOk.create("新增服务审计日志成功");
    }


    /**
     * 服务审计日志表查询
     *
     * @param clusterAuditQueryVO
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUDIT_LOG_QUERY_URL)
    public ResponseEntity<?> queryDataClusterAudit(ClusterAuditQueryVO clusterAuditQueryVO, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<AuditLog> list = clusterAuditService.queryAll(clusterAuditQueryVO);
            int count = clusterAuditService.countAll(clusterAuditQueryVO);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            log.error("获取服务审计日志列表失败", e);
            return ResponseError.create("获取服务审计日志列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 服务审计日志表详情查询
     *
     * @param
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUDIT_LOG_GET_URL)
    public ResponseEntity<?> getDataClusterAudit(@RequestParam String id) {
        AuditLog auditLog = null;
        try {
            if (StringUtils.isNotEmpty(id)) {
                auditLog = clusterAuditService.query(Integer.parseInt(id));
            }
        } catch (Exception e) {
            log.error("获取服务审计日志失败", e);
            return ResponseError.create("获取服务审计日志失败");
        }
        return ResponseOk.create(auditLog);
    }

}
