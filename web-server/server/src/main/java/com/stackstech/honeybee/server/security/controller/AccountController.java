package com.stackstech.honeybee.server.security.controller;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DataServiceController
 * @author william
 */
//TODO GYF
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class AccountController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/security/account/get")
    public ResponseMap<?> getAccount() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/security/account/delete")
    public ResponseMap<?> deleteAccount() {
        return null;
    }

    @PostMapping(value = "/security/account/update")
    public ResponseMap<?> updateAccount() {
        return null;
    }

    @PostMapping(value = "/security/account/add")
    public ResponseMap<?> addAccount() {
        return null;
    }

    @PostMapping(value = "/security/account/query")
    public ResponseMap<?> queryAccount() {
        return null;
    }

    @PostMapping(value = "/security/role/get")
    public ResponseMap<?> getAccountRole() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/security/role/delete")
    public ResponseMap<?> deleteAccountRole() {
        return null;
    }

    @PostMapping(value = "/security/role/update")
    public ResponseMap<?> updateAccountRole() {
        return null;
    }

    @PostMapping(value = "/security/role/add")
    public ResponseMap<?> addAccountRole() {
        return null;
    }

    @PostMapping(value = "/security/role/query")
    public ResponseMap<?> queryAccountRole() {
        return null;
    }
}
