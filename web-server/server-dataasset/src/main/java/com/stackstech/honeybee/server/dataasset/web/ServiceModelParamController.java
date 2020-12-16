package com.stackstech.honeybee.server.dataasset.web;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.server.dataasset.api.ApiUrls;
import com.stackstech.honeybee.server.dataasset.service.ServiceModelParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 模型参数Controller
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_MODEL_URI)
public class ServiceModelParamController {

    @Autowired
    private ServiceModelParamService serviceModelParamService;

    /**
     * 删除模型属性
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICE_DEL_MODEL_PARAM_URI)
    public ResponseEntity<?> delModelParam(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (StringUtils.isNotEmpty(id)) {
                return serviceModelParamService.delete(Long.parseLong(id));
            } else {
                return ResponseError.create(400, "请求参数为空");
            }
        } catch (Exception e) {
            return ResponseError.create(500, "删除模型参数失败");
        }
    }
}
