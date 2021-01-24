package com.stackstech.honeybee.server.assets.controller;

import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.service.AssetsCatalogService;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.vo.*;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private AssetsCatalogService assetsCatalogService;

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
    public ResponseMap<?> updateModel(@Valid @RequestBody AssetsModelVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AssetsModelEntity entity = new AssetsModelEntity().update(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!assetsModelService.update(entity)) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseMap<?> addModel(@Valid @RequestBody AssetsModelVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AssetsModelEntity entity = new AssetsModelEntity().build(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!assetsModelService.add(entity)) {
            return ResponseMap.failed("insert data service failed.");
        }
        return ResponseMap.success(true);
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
    @RequestMapping(value = "/data/assets/{catalogType}/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("catalogType") String catalogType, @PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getAssetsCatalog(catalogType, id));
    }

    @ApiOperation(value = "delete data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/{catalogType}/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("catalogType") String catalogType,
                                        @PathVariable("id") long id,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(assetsCatalogService.deleteAssetsCatalog(catalogType, id, account.getId()));
    }

    @ApiOperation(value = "update data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/{catalogType}/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@PathVariable("catalogType") String catalogType,
                                        @Valid @RequestBody AssetsCatalogVo vo,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.updateAssetsCatalog(catalogType, vo, account.getId())) {
            return ResponseMap.failed("update data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/{catalogType}/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@PathVariable("catalogType") String catalogType,
                                     @Valid @RequestBody AssetsCatalogVo vo,
                                     @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.addAssetsCatalog(catalogType, vo, account.getId())) {
            return ResponseMap.failed("insert data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query data assets catalog")
    @RequestMapping(value = "/data/assets/{catalogType}/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@PathVariable("catalogType") String catalogType,
                                       @Valid @RequestBody AssetsCatalogQuery parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.getAssetsCatalogs(parameters.getParameter());
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseMap.setTotal(data, data.size());
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "query data assets catalog tree")
    @RequestMapping(value = "/data/assets/catalog/tree", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@Valid @RequestBody AssetsCatalogQuery parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.getAssetsCatalogTree(parameters.getParameter());
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseMap.setTotal(data, data.size());
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get recycler data")
    @RequestMapping(value = "/data/assets/recycler/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getRecycler(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getDataRecycler(id));
    }

    @ApiOperation(value = "delete recycler data")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/recycler/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteRecycler(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(assetsCatalogService.deleteDataRecycler(id, account.getId()));
    }

    @ApiOperation(value = "query recycler data")
    @RequestMapping(value = "/data/assets/recycler/query", method = RequestMethod.POST)
    public ResponseMap<?> queryRecycler(@Valid @RequestBody PageQuery parameters) {
        List<DataRecyclerEntity> data = assetsCatalogService.getDataRecyclers(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = assetsCatalogService.getDataRecyclerCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


}
