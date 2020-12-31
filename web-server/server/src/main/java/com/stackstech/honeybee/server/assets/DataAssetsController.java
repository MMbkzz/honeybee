package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.*;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * account assets service controller
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
    @Autowired
    private DataService<DataRecyclerEntity> recyclerService;

    @RequestMapping(value = "/data/assets/model/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getModel(@PathVariable("id") long id) {
        return ResponseMap.success(assetsModelService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/model/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteModel(@PathVariable("id") long id) {
        return ResponseMap.success(assetsModelService.delete(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/model/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateModel(@RequestBody AssetsModelEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!assetsModelService.update(entity)) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(true);
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseMap<?> addModel(@RequestBody AssetsModelEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!assetsModelService.add(entity)) {
            return ResponseMap.failed("insert data service failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/assets/model/query", method = RequestMethod.POST)
    public ResponseMap<?> queryModel(@RequestBody RequestParameter parameters) {
        List<AssetsModelEntity> data = assetsModelService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = assetsModelService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @RequestMapping(value = "/data/assets/catalog/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.delete(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@RequestBody AssetsCatalogEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!assetsCatalogService.update(entity)) {
            return ResponseMap.failed("update data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@RequestBody AssetsCatalogEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!assetsCatalogService.add(entity)) {
            return ResponseMap.failed("insert data assets catalog failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@RequestBody RequestParameter parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = assetsCatalogService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


    @RequestMapping(value = "/data/assets/recycler/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getRecycler(@PathVariable("id") long id) {
        return ResponseMap.success(recyclerService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/recycler/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteRecycler(@PathVariable("id") long id) {
        return ResponseMap.success(recyclerService.delete(id));
    }

    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/recycler/add", method = RequestMethod.PUT)
    public ResponseMap<?> addRecycler(@RequestBody DataRecyclerEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!recyclerService.add(entity)) {
            return ResponseMap.failed("insert recycler failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/assets/recycler/query", method = RequestMethod.POST)
    public ResponseMap<?> queryRecycler(@RequestBody RequestParameter parameters) {
        List<DataRecyclerEntity> data = recyclerService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = recyclerService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


}
