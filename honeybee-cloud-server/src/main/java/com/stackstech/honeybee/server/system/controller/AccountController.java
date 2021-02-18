package com.stackstech.honeybee.server.system.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.vo.AccountVo;
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
 * account and role service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @Autowired
    private BaseDataService<AccountEntity> accountService;

    @ApiOperation(value = "get account")
    @RequestMapping(value = "/security/account/get/{id}", method = RequestMethod.GET)
    public ResponseObject getAccount(@PathVariable("id") long id) {
        return ResponseObject.build().success(accountService.getSingle(id));
    }

    @ApiOperation(value = "delete account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/account/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteAccount(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(accountService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/account/update", method = RequestMethod.PUT)
    public ResponseObject updateAccount(@Validated({UpdateGroup.class}) @RequestBody AccountVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AccountEntity entity = new AccountEntity().update(account.getId()).copy(vo);

        if (!accountService.update(entity)) {
            return ResponseObject.build().failed(MessageHandler.of().message(MessageHandler.ACCOUNT_UPDATE_FAILED));
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/account/add", method = RequestMethod.PUT)
    public ResponseObject addAccount(@Validated({AddGroup.class}) @RequestBody AccountVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        AccountEntity entity = new AccountEntity().build(account.getId()).copy(vo);

        if (!accountService.add(entity)) {
            return ResponseObject.build().failed(MessageHandler.of().message(MessageHandler.ACCOUNT_INSERT_FAILED));
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query account")
    @RequestMapping(value = "/security/account/query", method = RequestMethod.POST)
    public ResponseObject queryAccount(@Validated @RequestBody PageQuery parameters) {
        List<AccountEntity> data = accountService.get(parameters.getParameter());
        int total = accountService.getTotalCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }

    @ApiOperation(value = "get account role")
    @RequestMapping(value = "/security/role/get/{id}", method = RequestMethod.GET)
    public ResponseObject getAcctRole(@PathVariable("id") long id) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "delete account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/role/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteAcctRole(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "update account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/role/update", method = RequestMethod.PUT)
    public ResponseObject updateAcctRole(@Validated @RequestBody DataServiceEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "add account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/role/add", method = RequestMethod.PUT)
    public ResponseObject addAcctRole(@Validated @RequestBody DataServiceEntity entity, @ApiIgnore @RequestAccount AccountEntity account) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "query account role")
    @RequestMapping(value = "/security/role/query", method = RequestMethod.POST)
    public ResponseObject queryAcctRole(@Validated @RequestBody PageQuery parameters) {
        //TODO WJ
        return null;
    }


}
