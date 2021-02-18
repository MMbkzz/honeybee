package com.stackstech.honeybee.server.assets.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogElement;
import com.stackstech.honeybee.server.assets.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.assets.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.assets.service.AssetsCatalogService;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogQuery;
import com.stackstech.honeybee.server.assets.vo.AssetsCatalogVo;
import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public ResponseObject getCatalog(@PathVariable("id") long id) {
        return ResponseObject.build().success(assetsCatalogService.getAssetsCatalog(id));
    }

    @ApiOperation(value = "delete data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/catalog/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteCatalog(@PathVariable("id") long id,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(assetsCatalogService.deleteAssetsCatalog(id, account.getId()));
    }

    @ApiOperation(value = "update data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/catalog/update", method = RequestMethod.PUT)
    public ResponseObject updateCatalog(@Validated({UpdateGroup.class}) @RequestBody AssetsCatalogVo vo,
                                        @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.updateAssetsCatalog(vo, account.getId())) {
            return ResponseObject.build().failed("data.update.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add data assets catalog")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/catalog/add", method = RequestMethod.PUT)
    public ResponseObject addCatalog(@Validated({AddGroup.class}) @RequestBody AssetsCatalogVo vo,
                                     @ApiIgnore @RequestAccount AccountEntity account) {
        if (!assetsCatalogService.addAssetsCatalog(vo, account.getId())) {
            return ResponseObject.build().failed("data.insert.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query data assets catalog")
    @RequestMapping(value = "/data/assets/catalog/query", method = RequestMethod.POST)
    public ResponseObject queryCatalog(@Validated @RequestBody AssetsCatalogQuery parameters) {
        List<AssetsCatalogEntity> data = assetsCatalogService.getAssetsCatalogs(parameters.getParameter());
        return ResponseObject.build().success(data, data.size());
    }

    @ApiOperation(value = "query data assets catalog list")
    @RequestMapping(value = "/data/assets/catalog/list", method = RequestMethod.GET)
    public ResponseObject queryCatalogList() {
        List<AssetsCatalogElement> data = assetsCatalogService.getAssetsCatalogList();
        return ResponseObject.build().success(data);
    }

    @ApiOperation(value = "get recycler data")
    @RequestMapping(value = "/data/assets/recycler/get/{id}", method = RequestMethod.GET)
    public ResponseObject getRecycler(@PathVariable("id") long id) {
        return ResponseObject.build().success(assetsCatalogService.getDataRecycler(id));
    }

    @ApiOperation(value = "delete recycler data")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/recycler/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteRecycler(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(assetsCatalogService.deleteDataRecycler(id, account.getId()));
    }

    @ApiOperation(value = "query recycler data")
    @RequestMapping(value = "/data/assets/recycler/query", method = RequestMethod.POST)
    public ResponseObject queryRecycler(@Validated @RequestBody PageQuery parameters) {
        List<DataRecyclerEntity> data = assetsCatalogService.getDataRecyclers(parameters.getParameter());
        int total = assetsCatalogService.getDataRecyclerCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }

}
