package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
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
 * account and role service controller
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private DataService<AccountEntity> accountService;


    @RequestMapping(value = "/security/account/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAccount(@PathVariable("id") long id) {
        return ResponseMap.success(accountService.getSingle(id));
    }

    @RequestMapping(value = "/security/account/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAccount(@PathVariable("id") long id) {
        return ResponseMap.success(accountService.delete(id));
    }

    @RequestMapping(value = "/security/account/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAccount(@RequestBody AccountEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!accountService.update(entity)) {
            return ResponseMap.failed("update account failed.");
        }
        return ResponseMap.success(true);
    }

    @RequestMapping(value = "/security/account/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAccount(@RequestBody AccountEntity entity) {
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

    @RequestMapping(value = "/security/account/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAccount(@RequestBody RequestParameter parameters) {
        List<AccountEntity> data = accountService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = accountService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @RequestMapping(value = "/security/role/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAcctRole(@PathVariable("id") long id) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/role/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAcctRole(@PathVariable("id") long id) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/role/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAcctRole(@RequestBody DataServiceEntity entity) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/role/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAcctRole(@RequestBody DataServiceEntity entity) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/role/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAcctRole(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }


}
