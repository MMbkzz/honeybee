package com.stackstech.honeybee.server.assets.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.assets.vo.AssetsModelQuery;
import com.stackstech.honeybee.server.assets.vo.AssetsModelVo;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.core.service.BaseDataService;
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
public class AssetsModelController {

    @Autowired
    private BaseDataService<AssetsModelEntity> assetsModelService;

    @ApiOperation(value = "get data assets model")
    @RequestMapping(value = "/data/assets/model/get/{id}", method = RequestMethod.GET)
    public ResponseObject getModel(@PathVariable("id") long id) {
        return ResponseObject.build().success(assetsModelService.getSingle(id));
    }

    @ApiOperation(value = "get data assets model meta")
    @RequestMapping(value = "/data/assets/model/meta/{id}", method = RequestMethod.GET)
    public ResponseObject getModelMeta(@PathVariable("id") long id) {
        AssetsModelEntity model = assetsModelService.getSingle(id);
        return ResponseObject.build().success(model.getDatasourceMeta());
    }

    @ApiOperation(value = "delete data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/assets/model/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteModel(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(assetsModelService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/assets/model/update", method = RequestMethod.PUT)
    public ResponseObject updateModel(@Validated @RequestBody AssetsModelVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AssetsModelEntity entity = new AssetsModelEntity().update(account.getId()).copy(vo);

        if (!assetsModelService.update(entity)) {
            return ResponseObject.build().failed("data.update.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add data assets model")
    @AuditOperation(type = AuditOperationType.ASSETS, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/assets/model/add", method = RequestMethod.PUT)
    public ResponseObject addModel(@Validated @RequestBody AssetsModelVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AssetsModelEntity entity = new AssetsModelEntity().build(account.getId()).copy(vo);

        if (!assetsModelService.add(entity)) {
            return ResponseObject.build().failed("data.insert.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query data assets model")
    @RequestMapping(value = "/data/assets/model/query", method = RequestMethod.POST)
    public ResponseObject queryModel(@Validated @RequestBody AssetsModelQuery parameters) {
        List<AssetsModelEntity> data = assetsModelService.get(parameters.getParameter());
        int total = assetsModelService.getTotalCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }

}
