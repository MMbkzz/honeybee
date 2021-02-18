package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.api.service.DataService;
import com.stackstech.honeybee.server.api.vo.DataServiceVo;
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
 * data api service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataServiceController {

    @Autowired
    private DataService service;

    @ApiOperation(value = "get data service")
    @RequestMapping(value = "/data/service/get/{id}", method = RequestMethod.GET)
    public ResponseObject get(@PathVariable("id") long id) {
        log.debug("get data service information.");
        return ResponseObject.build().success(service.getSingle(id));
    }

    @ApiOperation(value = "delete data service")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/data/service/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject delete(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        log.debug("delete data service ID {}.", id);
        return ResponseObject.build().success(service.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data service config")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/data/service/update", method = RequestMethod.PUT)
    public ResponseObject update(@Validated({UpdateGroup.class}) @RequestBody DataServiceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceEntity entity = new DataServiceEntity().update(account.getId()).copy(vo);

        if (!service.update(entity)) {
            return ResponseObject.build().failed("data.update.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add data service config")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/data/service/add", method = RequestMethod.PUT)
    public ResponseObject add(@Validated({AddGroup.class}) @RequestBody DataServiceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceEntity entity = new DataServiceEntity().build(account.getId()).copy(vo);

        if (!service.add(entity)) {
            return ResponseObject.build().failed("data.insert.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query data service")
    @RequestMapping(value = "/data/service/query", method = RequestMethod.POST)
    public ResponseObject query(@Validated @RequestBody PageQuery parameters) {
        List<DataServiceEntity> data = service.get(parameters.getParameter());
        int total = service.getTotalCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }

    @ApiOperation(value = "query data service list")
    @RequestMapping(value = "/data/service/list", method = RequestMethod.GET)
    public ResponseObject query() {
        return ResponseObject.build().success(service.getDataServiceList());
    }

    @RequestMapping(value = "/data/service/online/{id}", method = RequestMethod.GET)
    public ResponseObject online(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        //TODO setting data service online
        return null;
    }

    @RequestMapping(value = "/data/service/offline/{id}", method = RequestMethod.GET)
    public ResponseObject offline(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        //TODO setting data service offline
        return null;
    }

}
