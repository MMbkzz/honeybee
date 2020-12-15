package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.service.ServiceModelFieldService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 属性字段Controller
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_MODEL_URI)
public class ServiceModelFieldController {

    @Autowired
    private ServiceModelFieldService serviceModelFieldService;

    /**
     * 删除模型字段
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICE_DEL_MODEL_FIELD_URI)
    public ResponseEntity<?> delModelField(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (StringUtils.isNotEmpty(id)) {
                return serviceModelFieldService.delete(Long.parseLong(id));
            } else {
                return ResponseError.create(400, "请求参数为空");
            }
        } catch (Exception e) {
            return ResponseError.create(500, "删除模型字段失败");
        }
    }

}
