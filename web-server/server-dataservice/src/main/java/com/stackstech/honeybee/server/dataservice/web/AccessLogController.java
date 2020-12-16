package com.stackstech.honeybee.server.dataservice.web;


import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.core.page.PageUtil;
import com.stackstech.honeybee.server.dataservice.api.ApiUrls;
import com.stackstech.honeybee.server.dataservice.model.AccessLog;
import com.stackstech.honeybee.server.dataservice.service.AccessLogService;
import com.stackstech.honeybee.server.dataservice.vo.AccessLogQueryVO;
import com.stackstech.honeybee.server.dataservice.vo.AccessLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据服务API访问日志
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_URI)
public class AccessLogController {

    private final Logger log = LoggerFactory.getLogger(AccessLogController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 新增数据服务API访问日志接口
     *
     * @param accessLog
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_ACCESS_LOG_ADD_URI)
    public ResponseEntity<?> addAccessLog(@RequestBody AccessLog accessLog) {
        try {
            int i = accessLogService.add(accessLog);
            if (i <= 0) {
                return ResponseError.create(500, "新增数据服务API访问日志失败");
            }
        } catch (Exception e) {
            log.error("新增数据服务API访问日志失败", e);
            return ResponseError.create(500, "新增数据服务API访问日志失败");
        }
        return ResponseOk.create("新增数据服务API访问日志成功");
    }

    /**
     * 数据服务API访问日志查询列表接口
     *
     * @param accessLogQueryVO
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_ACCESS_LOG_QUERY_URI)
    public ResponseEntity<?> queryAccessLog(AccessLogQueryVO accessLogQueryVO, Page page) {
        Map<String, Object> map;
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            map = accessLogService.queryAll(accessLogQueryVO);
        } catch (Exception e) {
            log.error("数据服务访问日志查询列表失败", e);
            return ResponseError.create(500, "数据服务访问日志查询列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 查询数据数据服务API访问日志详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_ACCESS_LOG_GET_URI)
    public ResponseEntity<?> getAccessLog(@RequestParam String id) {
        AccessLogVO accessLog = null;
        try {
            if (StringUtils.isNotEmpty(id)) {
                accessLog = accessLogService.query(Integer.parseInt(id));
            }
        } catch (Exception e) {
            log.error("获取数据服务Api详情失败", e);
            return ResponseError.create(500, "获取数据服务Api详情失败");
        }
        return ResponseOk.create(accessLog);
    }

    /**
     * 编辑数据服务API访问日志
     *
     * @param accessLog
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_ACCESS_LOG_UPDATE_URI)
    public ResponseEntity<?> editAccessLog(@RequestBody AccessLog accessLog) {
        try {
            int i = accessLogService.update(accessLog);
            if (i <= 0) {
                return ResponseError.create(500, "更新数据服务Api详情失败");
            }
        } catch (Exception e) {
            log.error("更新数据服务Api详情失败", e);
            return ResponseError.create(500, "更新数据服务Api详情失败");
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 删除数据服务API访问日志
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_ACCESS_LOG_DELETE_URI)
    public ResponseEntity<?> delAccessLog(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (null != id && !"".equals(id)) {
                int i = accessLogService.delete(Integer.valueOf(id));
                if (i <= 0) {
                    return ResponseError.create("删除数据服务API访问日志失败");
                }
            }
        } catch (Exception e) {
            log.error("删除数据服务API访问日志失败", e);
            return ResponseError.create("删除数据服务API访问日志失败");
        }
        return ResponseOk.create("删除数据服务APi访问日志成功");
    }
}
