package com.stackstech.honeybee.server.controller;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.entity.*;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.vo.AssetsCatalogQuery;
import com.stackstech.honeybee.server.core.vo.AssetsModelQuery;
import com.stackstech.honeybee.server.core.vo.PageQuery;
import com.stackstech.honeybee.server.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * account assets service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataAssetsController {

    @Autowired
    private DataService<AssetsModelEntity> assetsModelService;
    @Autowired
    private DataService<AssetsCatalogEntity> assetsCatalogService;
    @Autowired
    private DataService<DataRecyclerEntity> recyclerService;

    @ApiOperation(value = "get data assets model")
    @RequestMapping(value = "/data/assets/model/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getModel(@PathVariable("id") long id) {
        return ResponseMap.success(assetsModelService.getSingle(id));
    }

    @ApiOperation(value = "delete data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/model/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteModel(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(assetsModelService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/model/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateModel(@Valid @RequestBody AssetsModelEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsModelService.update(entity, account.getId())) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseMap<?> addModel(@Valid @RequestBody AssetsModelEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsModelService.add(entity, account.getId())) {
            return ResponseMap.failed("insert data service failed.");
        }
        return ResponseMap.success(entity);
    }

    @ApiOperation(value = "query data assets model")
    @RequestMapping(value = "/data/assets/model/query", method = RequestMethod.POST)
    public ResponseMap<?> queryModel(@Valid @RequestBody AssetsModelQuery parameters) {
        List<AssetsModelEntity> data = assetsModelService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = assetsModelService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get data assets catalog")
    @RequestMapping(value = "/data/assets/catalog/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getSingle(id));
    }

    @ApiOperation(value = "delete data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(assetsCatalogService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@Valid @RequestBody AssetsCatalogEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.update(entity, account.getId())) {
            return ResponseMap.failed("update data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@Valid @RequestBody AssetsCatalogEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.add(entity, account.getId())) {
            return ResponseMap.failed("insert data assets catalog failed.");
        }
        return ResponseMap.success(entity);
    }

    @ApiOperation(value = "query data assets catalog")
    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@Valid @RequestBody AssetsCatalogQuery parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = assetsCatalogService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get recycler data")
    @RequestMapping(value = "/data/assets/recycler/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getRecycler(@PathVariable("id") long id) {
        return ResponseMap.success(recyclerService.getSingle(id));
    }

    @ApiOperation(value = "delete recycler data")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/recycler/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteRecycler(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(recyclerService.delete(id, account.getId()));
    }

    @ApiOperation(value = "add recycler data")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/recycler/add", method = RequestMethod.PUT)
    public ResponseMap<?> addRecycler(@Valid @RequestBody DataRecyclerEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!recyclerService.add(entity, account.getId())) {
            return ResponseMap.failed("insert recycler failed.");
        }
        return ResponseMap.success(entity);
    }

    @ApiOperation(value = "query recycler data")
    @RequestMapping(value = "/data/assets/recycler/query", method = RequestMethod.POST)
    public ResponseMap<?> queryRecycler(@Valid @RequestBody PageQuery parameters) {
        List<DataRecyclerEntity> data = recyclerService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = recyclerService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


}
