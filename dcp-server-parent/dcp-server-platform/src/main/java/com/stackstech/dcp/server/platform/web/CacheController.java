package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.service.CacheService;
import com.stackstech.dcp.server.platform.vo.CacheVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 缓存管理Controller
 */
@RestController
@RequestMapping(ApiUrls.API_CACHE_URI)
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private CacheService cacheService;

    /**
     * 缓存管理查询
     *
     * @param key
     * @return
     */
    @GetMapping(value = ApiUrls.API_CACHE_QUERY_URI)
    public ResponseEntity<?> queryCaches(String key) {
        Map<String, Object> map = null;
        try {
            map = cacheService.queryAll(key);
        } catch (Exception e) {
            logger.error("获取缓存失败", e);
            return ResponseError.create(500, "获取缓存列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 缓存管理查询
     *
     * @param cacheVO
     * @return
     */
    @PostMapping(value = ApiUrls.API_CACHE_GET_URI)
    public ResponseEntity<?> getCache(@RequestBody CacheVO cacheVO) {
        CacheVO cache = null;
        try {
            cache = cacheService.queryByKey(cacheVO);
        } catch (Exception e) {
            logger.error("获取缓存详情失败", e);
            return ResponseError.create(500, "获取缓存详情失败");
        }
        return ResponseOk.create(cache);
    }

    /**
     * 缓存管理查询
     *
     * @param keys
     * @return
     */
    @PostMapping(value = ApiUrls.API_CACHE_DEL_URI)
    public ResponseEntity<?> delCaches(@RequestBody List<String> keys) {
        if (keys == null || keys.size() == 0) {
            return ResponseError.create("请求参数为空");
        }
        try {
            cacheService.delete(keys);
        } catch (Exception e) {
            logger.error("获取缓存失败", e);
            return ResponseError.create(500, "删除缓存失败");
        }
        return ResponseOk.create("删除缓存成功");
    }
}
