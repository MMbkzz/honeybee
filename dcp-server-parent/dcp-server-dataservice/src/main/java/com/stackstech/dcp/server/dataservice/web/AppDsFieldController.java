package com.stackstech.dcp.server.dataservice.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.dataservice.api.ApiUrls;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.service.AppDsFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据服务APP授权字段
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_URI)
public class AppDsFieldController {

    private final Logger log = LoggerFactory.getLogger(AppDsFieldController.class);

    @Autowired
    private AppDsFieldService appDsFieldService;

    /**
     * 新增数据服务APP授权字段接口
     *
     * @param appDsField
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_FIELD_ADD_URI)
    public ResponseEntity<?> addAppDsField(@RequestBody AppDsField appDsField) {

        try {
            int i = appDsFieldService.add(appDsField);
            if (i <= 0) {
                return ResponseError.create(500, "新增数据服务API授权字段失败");
            }
        } catch (Exception e) {
            log.error("新增数据服务APP授权字段失败", e);
            return ResponseError.create(400, "新增数据服务APP授权字段失败");
        }
        return ResponseOk.create("result", "新增数据服务API授权字段成功");
    }

    /**
     * 数据服务APP授权字段查询列表接口
     *
     * @param appDsField
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_DS_FIELD_QUERY_URI)
    public ResponseEntity<?> getPageAppDsField(AppDsField appDsField, Page page) {
        List<AppDsField> result = appDsFieldService.queryAll(appDsField);
        return ResponseOk.create(result);
    }

    /**
     * 查询数据服务APP授权字段详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_DS_FIELD_GET_URI)
    public ResponseEntity<?> getAppDsFieldInfo(@RequestParam String id) {
        AppDsField AppDsField = appDsFieldService.query(Long.valueOf(id));
        return ResponseOk.create(AppDsField);
    }

    /**
     * 编辑数据服务APP授权字段
     *
     * @param appDsField
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_FIELD_UPDATE_URI)
    public ResponseEntity<?> editAppDsField(@RequestBody AppDsField appDsField) {
        appDsFieldService.update(appDsField);
        return ResponseOk.create("result", "OK");
    }

    /**
     * 删除数据服务APP授权字段
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DS_FIELD_DELETE_URI)
    public ResponseEntity<?> delAppDsField(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("appId");
            if (null != id && !"".equals(id)) {
                appDsFieldService.delete(Long.valueOf(id));
            }
            return ResponseOk.create("result", "OK");
        } catch (Exception e) {
            log.error("删除数据服务APP授权字段失败", e);
            return ResponseError.create("删除数据服务APP授权字段失败");
        }
    }
}
