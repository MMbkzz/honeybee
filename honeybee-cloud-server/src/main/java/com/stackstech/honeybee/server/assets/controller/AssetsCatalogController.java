package com.stackstech.honeybee.server.assets.controller;

import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogElement;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.service.AssetsCatalogService;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogQuery;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
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
public class AssetsCatalogController {

    @Autowired
    private AssetsCatalogService assetsCatalogService;

    @ApiOperation(value = "get data assets catalog")
    @RequestMapping(value = "/data/assets/catalog/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getCatalog(@PathVariable("id") long id) {
        return ResponseMap.success(assetsCatalogService.getAssetsCatalog(id));
    }

    @ApiOperation(value = "delete data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteCatalog(@PathVariable("id") long id,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(assetsCatalogService.deleteAssetsCatalog(id, account.getId()));
    }

    @ApiOperation(value = "update data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateCatalog(@Valid @RequestBody AssetsCatalogVo vo,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.updateAssetsCatalog(vo, account.getId())) {
            return ResponseMap.failed("update data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseMap<?> addCatalog(@Valid @RequestBody AssetsCatalogVo vo,
                                     @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.addAssetsCatalog(vo, account.getId())) {
            return ResponseMap.failed("insert data assets catalog failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query data assets catalog")
    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalog(@Valid @RequestBody AssetsCatalogQuery parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.getAssetsCatalogs(parameters.getParameter());
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseMap.setTotal(data, data.size());
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "query data assets catalog list")
    @RequestMapping(value = "/data/assets/catalog/list", method = RequestMethod.POST)
    public ResponseMap<?> queryCatalogList() {
        List<AssetsCatalogElement> data = assetsCatalogService.getAssetsCatalogList();
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseMap.success(data);
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
