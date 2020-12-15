package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.dataasset.model.ModelCode;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.model.SysConfig;
import com.stackstech.dcp.server.platform.service.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统参数Controller
 */
@RestController
@RequestMapping(ApiUrls.API_CONFIG_URI)
public class SysConfigController {

    private static final Logger logger = LoggerFactory.getLogger(SysConfigController.class);

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 调用ModelCodeController的cluster_sub_type
     * 查询配置参数<Code></>
     *
     * @return
     */
    @GetMapping(ApiUrls.API_CONFIG_QUERY_URI)
    public ResponseEntity<?> queryConfig(ModelCode modelCode) {
        List<Map<String, Object>> list = null;
        try {
            list = sysConfigService.queryAll(modelCode);
        } catch (Exception e) {
            logger.error("获取配置参数列表失败", e);
            return ResponseError.create(500, "获取参数配置列表失败");
        }
        return ResponseOk.create(list);
    }

    /**
     * 只读
     * 查询配置参数<Code></>
     *
     * @return
     */
    @GetMapping(ApiUrls.API_CONFIG_QUERY_CONFIG_URI)
    public ResponseEntity<?> query() {
        Map<String, Object> list = null;
        try {
            list = sysConfigService.queryConfig();
        } catch (Exception e) {
            logger.error("获取配置参数列表失败", e);
            return ResponseError.create(500, "获取参数配置列表失败");
        }
        return ResponseOk.create(list);
    }


    /**
     * 新增参数配置
     *
     * @param sysConfig
     * @return
     */
    @PostMapping(ApiUrls.API_CONFIG_ADD_URI)
    public ResponseEntity<?> addConfig(@RequestBody SysConfig sysConfig) {
        try {
            int i = sysConfigService.add(sysConfig);
            if (i <= 0) {
                return ResponseError.create(500, "新增参数失败");
            }
        } catch (Exception e) {
            logger.error("新增配置参数失败", e);
            return ResponseError.create(500, "新增参数配置失败");
        }
        return ResponseOk.create("新增参数成功");
    }

    /**
     * 修改参数配置
     *
     * @param sysConfig
     * @return
     */
    @PostMapping(ApiUrls.API_CONFIG_EDIT_URI)
    public ResponseEntity<?> updateConfig(@RequestBody SysConfig sysConfig) {
        try {
            int i = sysConfigService.update(sysConfig);
            if (i <= 0) {
                return ResponseError.create(500, "修改参数失败");
            }
        } catch (Exception e) {
            logger.error("修改配置参数失败", e);
            return ResponseError.create(500, "修改参数配置失败");
        }
        return ResponseOk.create("修改参数成功");
    }

    /**
     * 删除参数配置
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_CONFIG_DEL_URI)
    public ResponseEntity<?> delConfig(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            int i = sysConfigService.delete(Long.valueOf(id));
            if (i <= 0) {
                return ResponseError.create(500, "删除参数失败");
            }
        } catch (Exception e) {
            logger.error("删除配置参数失败", e);
            return ResponseError.create(500, "删除参数配置失败");
        }
        return ResponseOk.create("删除参数成功");
    }
}
