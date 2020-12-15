package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.model.DataAssetArea;
import com.stackstech.dcp.server.dataasset.service.DataAssetAreaService;
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
 * 数据资产分类
 */
@RestController
@RequestMapping(ApiUrls.API_DATAASSET_AREA_URI)
public class AssetAreaController {

    private final Logger log = LoggerFactory.getLogger(AssetAreaController.class);

    @Autowired
    private DataAssetAreaService dataAssetAreaService;

    /**
     * 新增资产领域接口
     *
     * @param dataAssetArea
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_ADD_AREA_URI)
    public ResponseEntity<?> addAssetArea(@RequestBody DataAssetArea dataAssetArea, HttpServletRequest req) {
        try {
            int i = dataAssetAreaService.add(dataAssetArea, req);
            if (i <= 0) {
                return ResponseError.create(500, "新增数据资产领域失败");
            }
        } catch (Exception e) {
            log.error("新增数据资产领域失败", e);
            return ResponseError.create(500, "新增数据资产领域失败");
        }
        return ResponseOk.create("新增数据资产领域成功");
    }

    /**
     * 资产领域查询列表接口
     *
     * @param dataAssetArea
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_DATAASSET_QUERY_AREA_URI)
    public ResponseEntity<?> queryAssetAreas(DataAssetArea dataAssetArea, Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<DataAssetArea> result = dataAssetAreaService.queryAll(dataAssetArea);
            int count = dataAssetAreaService.countAll(dataAssetArea);
            map.put("list", result);
            map.put("count", count);
        } catch (Exception e) {
            log.error("获取资产领域列表失败", e);
            return ResponseError.create(500, "获取资产领域列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 查询数据资产领域详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_DATAASSET_GET_AREA_URI)
    public ResponseEntity<?> getAssetArea(@RequestParam String id) {
        DataAssetArea assetArea = null;
        try {
            assetArea = dataAssetAreaService.query(String.valueOf(id));
        } catch (Exception e) {
            log.error("获取资产领域失败", e);
            return ResponseError.create(500, "获取资产领域失败");
        }
        return ResponseOk.create(assetArea);
    }

    /**
     * 编辑资产领域
     *
     * @param dataAssetArea
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_EDIT_AREA_URI)
    public ResponseEntity<?> editAssetArea(@RequestBody DataAssetArea dataAssetArea, HttpServletRequest req) {
        try {
            int i = dataAssetAreaService.update(dataAssetArea, req);
            if (i <= 0) {
                return ResponseError.create("更新资产领域失败");
            }
        } catch (Exception e) {
            log.error("更新资产领域失败", e);
            return ResponseError.create("更新资产领域失败");
        }
        return ResponseOk.create("更新资产领域成功");
    }

    /**
     * 删除资产领域
     *
     * @param ids
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_DEL_AREA_URI)
    public ResponseEntity<?> delAssetArea(@RequestBody List<String> ids) {
        try {
            return dataAssetAreaService.delete(ids);
        } catch (Exception e) {
            log.error("删除资产领域失败", e);
            return ResponseError.create(500, "删除资产领域失败");
        }
    }

    @GetMapping(ApiUrls.API_DATAASSET_CHECK_AREA_URI)
    public ResponseEntity checkArea(String areaName) {
        try {
            DataAssetArea assetArea = dataAssetAreaService.queryByName(areaName);
            if (assetArea != null) {
                return ResponseError.create(500, "资产领域已经存在!");
            }
        } catch (Exception e) {
            log.error("资产领域校验失败..", e);
            return ResponseError.create(500, "资产领域校验失败");
        }
        return ResponseOk.create("资产领域不存在!");
    }
}
