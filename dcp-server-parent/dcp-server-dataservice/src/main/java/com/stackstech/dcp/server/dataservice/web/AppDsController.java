package com.stackstech.dcp.server.dataservice.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.dataservice.api.ApiUrls;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.service.AppDsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据服务APP授权
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_URI)
public class AppDsController {

    private final Logger log = LoggerFactory.getLogger(AppDsController.class);

    @Autowired
    private AppDsService appDsService;

    /**
     * 新增数据服务APP授权接口
     *
     * @param appDs
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_ADD_URI)
    public ResponseEntity<?> addAppDs(@RequestBody AppDs appDs) {

        try {
            int i = appDsService.add(appDs);
            if (i <= 0) {
                return ResponseError.create(500, "新增数据服务API授权失败");
            }
        } catch (Exception e) {
            log.error("新增数据服务APP授权失败", e);
            return ResponseError.create(400, "新增数据服务APP授权失败");
        }
        return ResponseOk.create("result", "新增数据服务API授权成功");
    }

    /**
     * 数据服务APP授权查询列表接口
     *
     * @param appDs
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_DS_QUERY_URI)
    public ResponseEntity<?> getPageAppDs(AppDs appDs, Page page) {
        List<AppDs> result = appDsService.queryAll(appDs);
        return ResponseOk.create(result);
    }

    /**
     * 查询数据服务APP授权详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_DS_GET_URI)
    public ResponseEntity<?> getAppDsInfo(@RequestParam Long id) {
        AppDs appDs = appDsService.query(Long.valueOf(id));
        return ResponseOk.create(appDs);
    }

    /**
     * 编辑数据服务APP授权
     *
     * @param appDs
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_UPDATE_URI)
    public ResponseEntity<?> editAppDs(@RequestBody AppDs appDs) {
        appDsService.update(appDs);
        return ResponseOk.create("result", "OK");
    }

    /**
     * 删除数据服务APP授权
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_DELETE_URI)
    public ResponseEntity<?> delAppDs(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("appId");
            if (null != id && !"".equals(id)) {
                appDsService.delete(Long.valueOf(id));
            }
            return ResponseOk.create("result", "OK");
        } catch (Exception e) {
            log.error("删除数据服务APP授权失败", e);
            return ResponseError.create("删除数据服务APP授权失败");
        }
    }
}
