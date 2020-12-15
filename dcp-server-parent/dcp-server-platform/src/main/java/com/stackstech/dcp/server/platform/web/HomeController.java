package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 首页Controller
 */
@RestController
@RequestMapping(ApiUrls.API_HOME_URI)
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    /**
     * 首页指标概览
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_HOME_QUERY_URI)
    public ResponseEntity<?> home() {
        Map<String, Object> map = null;
        try {
            map = homeService.queryIndex();
        } catch (Exception e) {
            logger.error("获取首页指标失败", e);
            return ResponseError.create(500, "获取首页指标失败");
        }
        return ResponseOk.create(map);
    }
}
