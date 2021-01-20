package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.vo.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * account and role service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @Autowired
    private DataService<AccountEntity> accountService;

    @ApiOperation(value = "get account")
    @RequestMapping(value = "/security/account/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAccount(@PathVariable("id") long id) {
        return ResponseMap.success(accountService.getSingle(id));
    }

    @ApiOperation(value = "delete account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/account/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAccount(@PathVariable("id") long id) {
        return ResponseMap.success(accountService.delete(id));
    }

    @ApiOperation(value = "update account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/account/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAccount(@Valid @RequestBody AccountEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!accountService.update(entity)) {
            return ResponseMap.failed("update account failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add account")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/account/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAccount(@Valid @RequestBody AccountEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!accountService.add(entity)) {
            return ResponseMap.failed("insert account failed.");
        }
        return ResponseMap.success(entity);
    }

    @ApiOperation(value = "query account")
    @RequestMapping(value = "/security/account/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAccount(@Valid @RequestBody PageQuery parameters) {
        List<AccountEntity> data = accountService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = accountService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get account role")
    @RequestMapping(value = "/security/role/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAcctRole(@PathVariable("id") long id) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "delete account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/role/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAcctRole(@PathVariable("id") long id) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "update account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/role/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAcctRole(@Valid @RequestBody DataServiceEntity entity) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "add account role")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/role/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAcctRole(@Valid @RequestBody DataServiceEntity entity) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "query account role")
    @RequestMapping(value = "/security/role/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAcctRole(@Valid @RequestBody PageQuery parameters) {
        //TODO WJ
        return null;
    }


}
