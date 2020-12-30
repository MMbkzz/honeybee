package com.stackstech.honeybee.server.assets;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DataServiceController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataAssetsController {

    private final Logger log = LoggerFactory.getLogger(DataAssetsController.class);

    @Autowired
    private DataService<AssetsModelEntity> assetsModelService;

    @Autowired
    private DataService<AssetsCatalogEntity> assetsCatalogService;

    @RequestMapping(value = "/data/assets/model/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getModel(@PathVariable("id") long id) {
        return ResponseMap.success(assetsModelService.getSingle(id));
    }

    @RequestMapping(value = "/data/assets/model/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteModel(@PathVariable("id") long id) {
        return ResponseMap.success(assetsModelService.delete(id));
    }

    @RequestMapping(value = "/data/assets/model/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateModel(@RequestBody AssetsModelEntity entity) {
        if (!assetsModelService.update(entity)) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(true);
    }

    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseMap<?> addModel(@RequestBody AssetsModelEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
        });
        if (!assetsModelService.add(entity)) {
            return ResponseMap.failed("insert data service failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/assets/model/query", method = RequestMethod.POST)
    public ResponseMap<?> queryModel(@RequestBody RequestParameter parameters) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageStart", parameters.getPageStart());
        params.put("pageSize", parameters.getPageSize());
        params.put("status", parameters.getStatus());
        params.put("keywords", parameters.getKeywords());
        params.put("order", parameters.getOrder());

        List<AssetsModelEntity> data = assetsModelService.get(params);
        if (data != null && data.size() > 0) {
            int total = assetsModelService.getTotalCount(params);
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @RequestMapping(value = "/data/assets/catalog/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getSingle(id));
    }

    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.delete(id));
    }

    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@RequestBody AssetsCatalogEntity entity) {
        if (!assetsCatalogService.update(entity)) {
            return ResponseMap.failed("update data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@RequestBody AssetsCatalogEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
        });
        if (!assetsCatalogService.add(entity)) {
            return ResponseMap.failed("insert data assets catalog failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@RequestBody RequestParameter parameters) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageStart", parameters.getPageStart());
        params.put("pageSize", parameters.getPageSize());
        params.put("status", parameters.getStatus());
        params.put("keywords", parameters.getKeywords());
        params.put("order", parameters.getOrder());

        List<AssetsCatalogEntity> data = assetsCatalogService.get(params);
        if (data != null && data.size() > 0) {
            int total = assetsCatalogService.getTotalCount(params);
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


}
