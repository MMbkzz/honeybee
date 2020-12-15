package com.stackstech.dcp.server.operation.web;


import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.operation.api.ApiUrls;
import com.stackstech.dcp.server.operation.service.SysAuditLogService;
import com.stackstech.dcp.server.operation.vo.SysAuditQueryVO;
import com.stackstech.dcp.server.operations.model.SysAuditLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台操作日志Controller
 */
@RestController
@RequestMapping(ApiUrls.API_BASE_URI)
public class SysAuditLogController {
    private final Logger log = LoggerFactory.getLogger(SysAuditLogController.class);

    @Autowired
    private SysAuditLogService sysAuditLogService;

    /**
     * 新增平台操作日志表
     *
     * @param sysAuditLog
     * @return
     */
    @PostMapping(value = ApiUrls.API_SYS_AUDIT_LOG_ADD_URI)
    public ResponseEntity<?> addSysAuditLog(@RequestBody SysAuditLog sysAuditLog) {
        try {
            int i = sysAuditLogService.add(sysAuditLog);
            if (i <= 0) {
                return ResponseError.create(500, "新增平台操作日志失败");
            }
        } catch (Exception e) {
            log.error("新增平台操作日志失败", e);
            return ResponseError.create(500, "新增平台操作日志失败");
        }
        return ResponseOk.create("新增平台操作日志成功");
    }


    /**
     * 平台操作日志表查询
     *
     * @param sysAuditQueryVO
     * @return
     */
    @GetMapping(value = ApiUrls.API_SYS_AUDIT_LOG_QUERY_URL)
    public ResponseEntity<?> querySysAuditLog(SysAuditQueryVO sysAuditQueryVO, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            map = sysAuditLogService.queryAll(sysAuditQueryVO);
        } catch (Exception e) {
            log.error("获取平台日志列表失败!", e);
            return ResponseError.create(500, "获取平台日志列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 平台操作日志表详情查询
     *
     * @param
     * @return
     */
    @GetMapping(value = ApiUrls.API_SYS_AUDIT_LOG_GET_URL)
    public ResponseEntity<?> getSysAuditLog(@RequestParam String id) {
        SysAuditLog sysAuditLog = null;
        try {
            if (StringUtils.isNotEmpty(id)) {
                sysAuditLog = sysAuditLogService.query(Long.parseLong(id));
            }
        } catch (Exception e) {
            log.error("获取平台日志详情失败！", e);
            return ResponseError.create(500, "获取平台日志失败");
        }
        return ResponseOk.create(sysAuditLog);
    }
}
