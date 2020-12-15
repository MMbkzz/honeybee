package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.service.AssetMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 资产地图Controller
 */
@RestController
@RequestMapping(ApiUrls.API_DATAASSET_MAP_URI)
public class AssetMapController {

    private static final Logger logger = LoggerFactory.getLogger(AssetMapController.class);

    @Autowired
    private AssetMapService assetMapService;

    /**
     * 查询资产地图
     *
     * @return
     */
    @GetMapping(ApiUrls.API_DATAASSET_MAP_QUERY_URI)
    public ResponseEntity<?> queryAssetMap() {
        Map<String, Object> map = null;
        try {
            map = assetMapService.queryAll();
        } catch (Exception e) {
            logger.error("查询资产地图失败..", e);
            return ResponseError.create(500, "查询资产地图失败");
        }
        return ResponseOk.create(map);
    }
}
