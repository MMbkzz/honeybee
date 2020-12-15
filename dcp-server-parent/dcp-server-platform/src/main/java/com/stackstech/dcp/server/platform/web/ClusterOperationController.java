package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.service.InstanceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务运维管理
 */
@RestController
@RequestMapping(ApiUrls.API_OPERATION_URI)
public class ClusterOperationController {

    private static final Logger logger = LoggerFactory.getLogger(ClusterOperationController.class);

    @Autowired
    private InstanceService instanceService;

    /**
     * 获取集群列表
     *
     * @param instance
     * @param page
     * @return
     */
    @GetMapping(ApiUrls.API_OPERATION_QUERY_URI)
    public ResponseEntity<?> queryOperations(Instance instance, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<Instance> list = instanceService.queryAll(instance);
            int count = instanceService.countAll(instance);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("获取集群列表失败", e);
            return ResponseError.create("获取集群列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 新增集群实例
     *
     * @param instance
     * @return
     */
    @PostMapping(ApiUrls.API_OPERATION_ADD_URI)
    public ResponseEntity<?> addOperation(@RequestBody Instance instance, HttpServletRequest req) {
        try {
            return instanceService.insert(instance, req);
        } catch (Exception e) {
            logger.error("新增集群实例失败", e);
            return ResponseError.create("新增集群实例失败");
        }
    }

    /**
     * 修改集群实例<状态>
     *
     * @param instance
     * @return
     */
    @PostMapping(ApiUrls.API_OPERATION_UPDATE_URI)
    public ResponseEntity<?> editOperation(@RequestBody Instance instance, HttpServletRequest req) {
        try {
            return instanceService.update(instance, req);
        } catch (Exception e) {
            logger.error("更新集群实例失败", e);
            return ResponseError.create("更新集群实例失败");
        }
    }

    /**
     * 更改数据源状态
     *
     * @param instance
     * @return
     */
    @PostMapping(ApiUrls.API_OPERATION_STATUS_URI)
    public ResponseEntity<?> changeServicesource(@RequestBody Instance instance, HttpServletRequest req) {
        try {
            return instanceService.changeStatus(instance, req);
        } catch (Exception e) {
            logger.error("集群实例状态更改失败....", e);
            return ResponseError.create(500, "集群实例状态更改失败");
        }
    }

    /**
     * 删除集群实例
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_OPERATION_DEL_URI)
    public ResponseEntity<?> delOperation(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            String id = (String) map.get("id");
            if (StringUtils.isNotEmpty(id)) {
                int i = instanceService.delete(id);
                if (i < 0) {
                    return ResponseError.create(500, "删除集群实例失败");
                }
            }
        } catch (Exception e) {
            logger.error("删除集群实例失败", e);
            return ResponseError.create(500, "删除集群实例失败");
        }
        return ResponseOk.create("删除集群实例成功");
    }


}
