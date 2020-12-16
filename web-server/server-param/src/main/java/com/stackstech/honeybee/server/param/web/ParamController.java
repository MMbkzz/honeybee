package com.stackstech.honeybee.server.param.web;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.server.param.api.ApiUrls;
import com.stackstech.honeybee.server.param.model.Parameter;
import com.stackstech.honeybee.server.param.service.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 参数Controller
 */
@RestController
@RequestMapping(ApiUrls.API_PARAM_URI)
public class ParamController {

    private static final Logger logger = LoggerFactory.getLogger(ParamController.class);

    @Autowired
    private ParameterService parameterService;

    /**
     * 获取参数列表
     *
     * @param parameter
     * @return
     */
    @GetMapping(ApiUrls.API_PARAM_QUERY_URI)
    public ResponseEntity<?> query(Parameter parameter) {
        List<Parameter> list = null;
        try {
            list = parameterService.queryAll(parameter);
        } catch (Exception e) {
            logger.error("获取参数列表失败...，", e);
            return ResponseError.create(500, "获取参数列表失败...");
        }
        return ResponseOk.create(list);
    }

    @PostMapping(ApiUrls.API_PARAM_DELETE_API)
    public ResponseEntity<?> deleteParam(@RequestBody Map<String, Object> map) {
        if (map.get("id") == null) {
            return ResponseError.create(400, "请求参数为空...");
        }
        try {
            String id = (String) map.get("id");
            int i = parameterService.delete(Long.parseLong(id));
            if (i <= 0) {
                return ResponseError.create(500, "删除参数失败...");
            }
        } catch (Exception e) {
            logger.error("delete param error! ", e);
        }
        return ResponseOk.create("删除成功");
    }


}
